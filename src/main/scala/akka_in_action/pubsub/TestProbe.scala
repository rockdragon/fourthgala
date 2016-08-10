package akka_in_action.pubsub

import akka.actor.Actor

class TestProbe extends Actor{
  def receive = {
    case msg => println(s"TestProbe received: $msg")
  }
}
