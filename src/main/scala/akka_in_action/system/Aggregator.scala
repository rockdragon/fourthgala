package akka_in_action.system

import akka.actor.Actor
import akka.remote.ContainerFormats.ActorRef

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.FiniteDuration

class Aggregator(timeout: FiniteDuration, pipe: ActorRef)
extends Actor{
  val messages = new ListBuffer[PhotoMessage]
  implicit val ec = context.system.dispatcher

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)
    messages.foreach(self ! _)
    messages.clear
  }

  def receive = {
    case rcvMsg: PhotoMessage => {
      messages.find(_.id == rcvMsg.id) match {
        case Some(alreadyRcvMsg) => {
          val newCombinedMsg = new PhotoMessage(
            rcvMsg.id,
            rcvMsg.photo,
            rcvMsg.creationTime.orElse(alreadyRcvMsg.creationTime),
            rcvMsg.speed.orElse(alreadyRcvMsg.speed)
          )
          pipe ! newCombinedMsg
          messages -= alreadyRcvMsg
        }
        case None => {
          messages += rcvMsg
          context.system.scheduler.scheduleOnce(
            timeout,
            self,
            TimeoutMessage(rcvMsg)
          )
        }
      }
    }
    case TimeoutMessage(rcvMsg) => {
      messages.find(_.id == rcvMsg.id) match {
        case Some(alreadyRcvMsg) => {
          pipe ! alreadyRcvMsg
          messages -= alreadyRcvMsg
        }
        case None => // message is already processed
      }
    }
    case ex: Exception => throw ex
  }
}
