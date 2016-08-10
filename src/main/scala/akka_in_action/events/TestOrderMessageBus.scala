package akka_in_action.events

import akka.actor.ActorSystem
import akka.testkit.TestProbe
import scala.concurrent.duration._

object TestOrderMessageBus extends App {
  implicit val system = ActorSystem("actor-system")
  import system.dispatcher

  val bus = new OrderMessageBus
  val singleBooks = TestProbe()

  bus.subscribe(singleBooks.ref, false)
  val multiBooks = TestProbe()

  bus.subscribe(multiBooks.ref, true)
  val msg = new Order("me", "Akka in action", 2)

  bus.publish(msg)
  singleBooks.expectMsg(msg)
  multiBooks.expectNoMsg(3 seconds)
  val msg2 = new Order("me", "Akka in action", 3)

  bus.publish(msg2)
  singleBooks.expectNoMsg(3 seconds)
  multiBooks.expectMsg(msg2)
}
