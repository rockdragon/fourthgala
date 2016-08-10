package akka_in_action.events

import akka.actor.Actor

class TestActor(name: String) extends Actor {
  def receive = {
    case msg: Any => println(s"[$name] receive $msg")
  }
}
