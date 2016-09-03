
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Balance, Broadcast, Flow, GraphDSL, Keep, Merge, RunnableGraph, Sink, Source, Tcp}
import GraphDSL.Implicits._
import akka.NotUsed
import akka.stream.scaladsl.Tcp.OutgoingConnection
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.util.ByteString
import akka_in_action.streams.SourceShape_.NumbersSource

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object SinkShape_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  class StdoutSink extends GraphStage[SinkShape[Int]] {
    val in: Inlet[Int] = Inlet("StdoutSink")
    override val shape: SinkShape[Int] = SinkShape(in)

    override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
      new GraphStageLogic(shape) {
        override def preStart(): Unit = pull(in)

        setHandler(in, new InHandler {
          @throws[Exception](classOf[Exception])
          override def onPush(): Unit = {
            println(s"grab: ${grab(in)}")
            pull(in)
          }
        })
      }
  }

  val sourceGraph: Graph[SourceShape[Int], NotUsed] = new NumbersSource
  val mySource: Source[Int, NotUsed] = Source.fromGraph(sourceGraph)

  val sinkGraph: Graph[SinkShape[Int], NotUsed] = new StdoutSink
  val mySink: Sink[Int, NotUsed] = Sink.fromGraph(sinkGraph)
  val result = mySource.take(66).to(mySink)
  result.run()

  system.terminate()
}
