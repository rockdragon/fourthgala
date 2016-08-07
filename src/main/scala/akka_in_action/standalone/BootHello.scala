package akka_in_action.standalone

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

class BootHello extends App {
  implicit val system = ActorSystem("hellokernel")
  import system.dispatcher

  val actor = system.actorOf(Props[HelloWorld])
  val config = system.settings.config
  val timer = ConfigFactory.load().getInt("helloworld.timer")
  system.actorOf(Props(new HelloWorldCaller(timer millis, actor)))
}
