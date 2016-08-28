
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Fusing}
import akka.stream.scaladsl.{Flow, Sink, Source}

object Fuses extends App {
  implicit val system = ActorSystem("actor-system")
  implicit val materializer = ActorMaterializer()

  val flow = Flow[Int].map(_ * 2).filter(_ > 50)
  val fused = Fusing.aggressive(flow)

  Source.fromIterator(() => Iterator from 0)
    .via(fused)
    .take(100)
    .to(Sink.foreach(println))
    .run()
}
