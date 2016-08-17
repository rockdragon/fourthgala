package akka_in_action.channels

import akka.actor.Actor

class Echo extends Actor {
  override def receive: Receive = {
    case msg => sender ! msg
  }
}
