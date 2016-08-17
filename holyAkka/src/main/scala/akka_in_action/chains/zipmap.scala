package akka_in_action.chains

import akka.actor.ActorSystem

import scala.concurrent.Future

object zipmap extends App {
  implicit val system = ActorSystem()
  import system.dispatcher

  val futureX = Future(6/0) recover {
    case _ => 0
  }
  val futureY = Future(10/0) recover {
    case _ => 1
  }

  futureX zip (futureY) map { case (x, y) =>
    println(x, y)
    system.shutdown
  }
}
