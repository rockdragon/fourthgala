package akka_in_action.lifecycle

import akka.actor.{ActorSystem, Props, ActorLogging, Actor}

class LifeCycleHook extends Actor
  with ActorLogging {
  println("Constructor")

  override def preStart() {
    println("preStart")
  }

  override def preRestart(reason: Throwable,
                          message: Option[Any]): Unit = {
    println("preRestart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("postRestart")
    super.postRestart(reason)
  }

  override def postStop() {
    println("postStop")
  }

  def receive = {
    case "restart" => throw new IllegalStateException("force restart")
    case msg: AnyRef => println("Receive")
  }
}

class TestActor extends Actor {
  def receive = {
    case _ => println("Received")
  }
}

object app extends App {
  implicit val system = ActorSystem("my-system")

  val testActor = system.actorOf(Props[TestActor], "Sender")
  val testActorRef = system.actorOf(Props[LifeCycleHook], "LifeCycleHook")
  testActorRef ! "restart"
  Thread.sleep(2000)

  testActorRef.tell("msg", testActor)
  system.stop(testActorRef)
  Thread.sleep(1000)
  system.stop(testActor)

  system.shutdown()
}
