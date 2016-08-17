package akka_in_action.chains

import akka.actor.ActorSystem

import scala.concurrent.Future

object combinator extends App {
  implicit val system = ActorSystem()
  import system.dispatcher

  val futureX = Future(6/0) recover {
    case _ => 0
  }
  val futureY = Future(10/0) recover {
    case _ => 1
  }

  val futures = Seq(futureX, futureY)

  val fastestResponse = Future.firstCompletedOf(futures)

  fastestResponse map { resp =>
    println(resp)
    Future(resp)
  }

  fastestResponse andThen {
    case resp =>
      println(resp)
      system.shutdown
  }
}
