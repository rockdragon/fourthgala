package akka_in_action.chains

import akka.actor.ActorSystem

import scala.concurrent.Future
import scala.util.{Failure, Success}

object recover extends App{
  implicit val system = ActorSystem()
  import system.dispatcher

  val future = Future(6/0) recover {
    case e => {
      println(e)
      0
    }
  }

  future onComplete {
    case Success(result) => println(result)
    case Failure(e) => println(e)
  }
  future andThen {
    case _ => system.shutdown
  }
}
