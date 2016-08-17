package akka_in_action.events

import akka.actor.{ActorSystem, Props}

object TestOrderMessageBus extends App {
  implicit val system = ActorSystem("actor-system")
  import system.dispatcher

  val bus = new OrderMessageBus

  val singleBooks = system.actorOf(Props(new TestActor("singleBooks")))
  bus.subscribe(singleBooks, false)

  val multiBooks =  system.actorOf(Props(new TestActor("multiBooks")))
  bus.subscribe(multiBooks, true)

  val msg = new Order("me", "Akka in action", 1)
  bus.publish(msg)

  val msg2 = new Order("me", "Akka in action", 3)
  bus.publish(msg2)
}
