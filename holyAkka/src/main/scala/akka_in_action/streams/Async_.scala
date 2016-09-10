
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

object Async_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  Source(1 to 3)
    .map { i => println(s"A: $i"); i }.async
    .map { i => println(s"B: $i"); i }.async
    .map { i => println(s"C: $i"); i }.async
    .runWith(Sink.ignore)

  system.terminate()
}
