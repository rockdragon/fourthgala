
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.{FlowShape, ActorMaterializer}
import akka.stream.scaladsl._

import scala.concurrent.Await
import scala.concurrent.duration._

object FlowShape_ extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val pairUpWithToString =
    Flow.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val broadcast = b.add(Broadcast[Int](2))
      val zip = b.add(Zip[Int, String]())

      broadcast.out(0).map(identity) ~> zip.in0
      broadcast.out(1).map(_.toString) ~> zip.in1

      FlowShape(broadcast.in, zip.out)
    })

  val pair = pairUpWithToString.runWith(Source(List(1)), Sink.head)
  val result = Await.result(pair._2, 300 millis)
  println(result)

  system.terminate
}
