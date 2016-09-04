
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

object MapShape_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  class Map[A, B](f: A => B) extends GraphStage[FlowShape[A, B]] {
    val in = Inlet[A]("Map.in")
    val out = Outlet[B]("Map.out")

    override val shape = FlowShape.of(in, out)

    override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
      new GraphStageLogic(shape) {
        setHandler(in, new InHandler {
          @scala.throws[Exception](classOf[Exception])
          override def onPush(): Unit = {
            push(out, f(grab(in)))
          }
        })

        setHandler(out, new OutHandler {
          @scala.throws[Exception](classOf[Exception])
          override def onPull(): Unit = {
            pull(in)
          }
        })
      }
  }

  val flowGraph: Graph[FlowShape[Int, Int], NotUsed] = new Map[Int, Int]((a:Int) => a + 1)
  val result = Source.fromIterator(() => Iterator from 5).via(flowGraph).to(Sink.foreach(println))
  result.run()

  Thread.sleep(300)

  system.terminate()
}
