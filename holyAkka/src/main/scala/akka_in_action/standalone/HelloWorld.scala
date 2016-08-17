package akka_in_action.standalone

import akka.actor.{Actor, ActorLogging}

class HelloWorld extends Actor
  with ActorLogging {
  def receive = {
    case msg: String =>
      val hello = "Hello %s".format(msg)
      sender ! hello
      log.info("Sent response {}", hello)
  }
}
