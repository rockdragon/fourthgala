package akka_in_action.pubsub

import akka.actor.ActorSystem
import akka.testkit.TestProbe
import scala.concurrent.duration._

object EventStream extends App {
  implicit val system = ActorSystem("akka-system")

  import system.dispatcher

  val deliverOrder = TestProbe()
  val giftModule = TestProbe()

  system.eventStream.subscribe(
    deliverOrder.ref,
    classOf[Order]
  )
  system.eventStream.subscribe(
    giftModule.ref,
    classOf[Order]
  )

  val msg = Order("me", "Akka in Action", 3)
  system.eventStream.publish(msg)

  deliverOrder.expectMsg(msg)
  giftModule.expectMsg(msg)

  system.eventStream.unsubscribe(giftModule.ref)
  system.eventStream.publish(msg)

  deliverOrder.expectMsg(msg)
  giftModule.expectNoMsg(3 seconds)
}
