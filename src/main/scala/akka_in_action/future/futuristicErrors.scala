package akka_in_action.future

import akka.actor.ActorSystem

import scala.concurrent._
import scala.util.{Success, Failure}
import scala.util.control.NonFatal

object futuristicErrors extends App {
  implicit val system = ActorSystem()
  import system.dispatcher

  val futureFail = future {
    throw new Exception("Error!")
  }

  futureFail onComplete {
    case Failure(NonFatal(e)) => println(e)
    case Success(value) => println(value)
  }

  futureFail andThen {
    case _ => system.shutdown
  }
}
