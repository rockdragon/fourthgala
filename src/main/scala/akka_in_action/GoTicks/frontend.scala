package akka_in_action.GoTicks

import akka.actor.{Props, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory

object frontend extends App{
  val config = ConfigFactory.load()
  val frontend = ActorSystem("frontend", config.getConfig("remote-frontend"))

  val path2Simple = "akka.tcp://backend@0.0.0.0:2551/user/tooSimple"

  class Young extends Actor {
    def receive = {
      case m => println(s"Young received ${m}")
    }
  }

  frontend.actorOf(Props[Young], "tooYoung")

  val simple  = frontend.actorSelection(path2Simple)
  simple ! "hello, 2 Simple"
}
