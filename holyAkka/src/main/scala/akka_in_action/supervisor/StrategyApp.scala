
package akka_in_action.supervisor

import scala.concurrent.duration._
import akka.actor.{Actor, ActorLogging, ActorSystem, OneForOneStrategy, Props}
import akka.actor.SupervisorStrategy._

class Supervisor extends Actor with ActorLogging {
  override def receive: Receive = {
    case "spawn" => context.actorOf(Props[Worker])
  }

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 30 seconds) {
      case _: ArithmeticException      => Resume
      case _: NullPointerException     => Restart
      case _: IllegalArgumentException => Stop
      case _: Exception                => Escalate
    }
}

class Worker extends Actor with ActorLogging {
  override def receive: Receive = ???
}

object StrategyApp extends App {
  implicit val system = ActorSystem("actor-system")
  import system.dispatcher

  system.actorOf(Props[Supervisor], "supervisor")
}
