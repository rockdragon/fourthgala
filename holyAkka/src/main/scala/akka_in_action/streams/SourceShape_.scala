
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Balance, Broadcast, Flow, GraphDSL, Keep, Merge, RunnableGraph, Sink, Source, Tcp}
import GraphDSL.Implicits._
import akka.NotUsed
import akka.stream.scaladsl.Tcp.OutgoingConnection
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.util.ByteString

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object SourceShape_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  class NumbersSource extends GraphStage[SourceShape[Int]] {
    val out: Outlet[Int] = Outlet("NumbersSource")

    override val shape: SourceShape[Int] = SourceShape(out)

      override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
      new GraphStageLogic(shape) {
        private var counter = 1

        setHandler(out, new OutHandler {
          @throws[Exception](classOf[Exception])
          override def onPull(): Unit = {
            push(out, counter)
            println(s"push $counter")
            counter += 1
          }
        })
      }
  }

  val sourceGraph: Graph[SourceShape[Int], NotUsed] = new NumbersSource
  val mySource: Source[Int, NotUsed] = Source.fromGraph(sourceGraph)
  val result1: Future[Int] = mySource.take(10).runReduce(_ + _)
  val result2: Future[Int] = mySource.take(100).runReduce(_ + _)

  val combined = for {
    r1 <- result1
    r2 <- result2
  } yield(r1, r2)

  val res = Await.result(combined, 300 millis)
  println(res)

  system.terminate()
}
