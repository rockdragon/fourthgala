package akka_in_action.GoTicks

import akka.actor._
import com.typesafe.config._

object backend extends App {
  val config = ConfigFactory.load()
  val backend = ActorSystem("backend", config.getConfig("remote-backend"))

  val path2Young = "akka.tcp://frontend@0.0.0.0:2552/user/tooYoung"

  class Simple extends Actor {
    def receive = {
      case m => {
        println(s"Simple received ${m}")
        val tooYoung = backend.actorSelection(path2Young)
        tooYoung ! "Howdy, idiot!"
      }
    }
  }

  backend.actorOf(Props[Simple], "tooSimple")
}
