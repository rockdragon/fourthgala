package akka_in_action.events

import akka.actor.{ActorSystem, DeadLetter, PoisonPill, Props}
import akka.testkit.TestActors.EchoActor

object TestDeadLetter extends App {
  implicit val system = ActorSystem("actor-system")
  import system.dispatcher

  val deadLetterMonitor = system.actorOf(Props(new TestActor("monitor")))

  system.eventStream.subscribe(
    deadLetterMonitor,
    classOf[DeadLetter]
  )

  val actor = system.actorOf(Props[EchoActor], "echo")
  actor ! PoisonPill

  val msg = new Order("me", "Akka in Action", 1)
  actor ! msg

  val msg2 = new Order("you", "how dare", 3)
  system.deadLetters ! msg2

  system.shutdown
}
