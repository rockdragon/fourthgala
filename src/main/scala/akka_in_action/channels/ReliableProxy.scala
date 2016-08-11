package akka_in_action.channels

import akka.actor.{Props, ActorSystem}
import akka.contrib.pattern.ReliableProxy
import scala.concurrent.duration._

object ReliableProxy extends App {
  implicit val system = ActorSystem("actor-system")
  import system.dispatcher

  val echo = system.actorSelection("akka.tcp://server:2501/user/echo")

  val proxy = system.actorOf(
    Props(new ReliableProxy(echo.anchorPath, 500.millis, Some(500.millis), Some(10))), "proxy")
}
