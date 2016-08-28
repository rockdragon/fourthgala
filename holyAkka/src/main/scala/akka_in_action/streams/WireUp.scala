
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object WireUp extends App {
  implicit val system = ActorSystem("actor-system")
  implicit val materializer = ActorMaterializer()

  Source(0 to 25)
    .via(Flow[Int].map(i => (i + 'A').asInstanceOf[Char]))
    .to(Sink.foreach(println(_))).run()

  system.terminate()

//  println(materializer.settings)
}
