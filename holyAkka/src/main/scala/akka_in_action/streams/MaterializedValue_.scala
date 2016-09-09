
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, FlowShape, SourceShape}
import akka.stream.scaladsl.{Flow, GraphDSL, Sink, Source}
import GraphDSL.Implicits._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object MaterializedValue_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val foldFlow: Flow[Int, Int, Future[Int]] = Flow.fromGraph(
    GraphDSL.create(Sink.fold[Int, Int](0)(_ + _)) { implicit builder => fold =>
      FlowShape(fold.in, builder.materializedValue.mapAsync(4)(identity).outlet)
    })
  val r = Source(1 to 10)
    .via(foldFlow)
    .runWith(Sink.head)
  println(Await.result(r, 200 millis))

  val cyclicFold: Source[Int, Future[Int]] = Source.fromGraph(
    GraphDSL.create(Sink.fold[Int, Int](0)(_ + _)) { implicit builder => fold =>
      Source(1 to 10) ~> fold
      SourceShape(builder.materializedValue.mapAsync(4)(identity).outlet)
    })
  val r2 = cyclicFold
    .via(foldFlow)
    .runWith(Sink.head)
  println(Await.result(r2, 200 millis))

  system.terminate()
}
