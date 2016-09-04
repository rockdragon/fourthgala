
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

object DuplicatorShape_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  class Duplicator[A] extends GraphStage[FlowShape[A, A]] {
    val in = Inlet[A]("Duplicator.in")
    val out = Outlet[A]("Duplicator.out")

    override val shape = FlowShape.of(in, out)

    override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
      new GraphStageLogic(shape) {
        var lastElem: Option[A] = None

        setHandler(in, new InHandler {
          @scala.throws[Exception](classOf[Exception])
          override def onPush(): Unit = {
            val elem = grab(in)
            lastElem = Some(elem)
            push(out, elem)
            println(s"[onPush][push($elem)]")
          }

          override def onUpstreamFinish(): Unit = {
            if (lastElem.isDefined) {
              emit(out, lastElem.get)
              println(s"[Finish] ${lastElem.get}")
            }
            complete(out)
          }
        })

        setHandler(out, new OutHandler {
          @scala.throws[Exception](classOf[Exception])
          override def onPull(): Unit = {
            if(lastElem.isDefined) {
              println(s"[onPull][push(${lastElem.get})]")
              push(out, lastElem.get)
              lastElem = None
            } else {
              println("[onPull][pull(in)]")
              pull(in)
            }
          }
        })
      }
  }

  val flowGraph: Graph[FlowShape[Int, Int], NotUsed] = new Duplicator[Int]
  val result = Source.fromIterator(() => Iterator from 5).via(flowGraph).to(Sink.ignore)
  result.run()

  Thread.sleep(50)

  system.terminate()
}
