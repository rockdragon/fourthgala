package akka_in_action.tests

import akka.actor.{Actor, ActorRef}

object SilentActorProtocol {
  case class SilentMessage(data: String)
  case class GetState(receiver: ActorRef)
}

class SilentActor extends Actor {
  import SilentActorProtocol._
  var internalState = Vector[String]()

  def receive = {
    case SilentMessage(data) =>
      internalState = internalState :+ data
  }

  def state = internalState
}
