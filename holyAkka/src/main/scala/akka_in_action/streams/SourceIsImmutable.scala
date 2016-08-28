
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

object SourceIsImmutable extends App {
  implicit val system = ActorSystem("actor-system")

  import system.dispatcher

  implicit val materializer = ActorMaterializer()

  val source = Source(1 to 10)
  source.map(_ => 0)
  // has no effect on source, since it's immutable
  val runnable1 = source.runWith(Sink.fold(0)(_ + _)) // 55

  val zeroes = source.map(_ => 0)
  // returns new Source[Int], with `map()` appended
  val runnable2 = zeroes.runWith(Sink.fold(0)(_ + _)) // 0

  Lib.consume(runnable1).andThen {
    case _ => Lib.consume(runnable2).andThen {
      case _ => system.terminate()
    }
  }
}

