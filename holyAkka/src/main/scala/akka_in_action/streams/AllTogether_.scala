
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Await
import scala.concurrent.duration._

object AllTogether_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val result = Source(1 to 5)
    .via(new FilterStage_.Filter(_ % 2 < 10))
    .via(new DuplicatorShape_.Duplicator())
    .via(new MapShape_.Map[Int, Int](_ / 2))
    .runWith(Sink.seq)

  val r = Await.result(result, 300 millis)
  println(r)

  system.terminate()
}
