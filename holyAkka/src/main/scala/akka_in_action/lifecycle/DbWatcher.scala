package akka_in_action.lifecycle

import akka.actor.{Terminated, Actor, ActorLogging, ActorRef}

class DbWatcher(dbWatcher: ActorRef) extends Actor with ActorLogging {
  context.watch(dbWatcher)

  def receive = {
    case Terminated(actorRef) =>
      log.warning("Actor {} terminated", actorRef)
  }
}