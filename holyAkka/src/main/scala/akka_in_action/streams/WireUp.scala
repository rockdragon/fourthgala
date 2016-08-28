
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object WireUp extends App {
  implicit val system = ActorSystem("actor-system")
  implicit val materializer = ActorMaterializer()

  Source('a' to 'g').via(Flow[Char].map(c => (c + 1).asInstanceOf[Char] )).to(Sink.foreach(println(_))).run()

//  println(materializer.settings)
}
