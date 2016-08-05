package GoTicks.com

import scala.concurrent.duration._
import akka.actor._

class RemoteLookup(path:String) extends Actor with ActorLogging {
  context.setReceiveTimeout(3 seconds)
  sendIdentifyRequest()
  def sendIdentifyRequest(): Unit = {
    val selection = context.actorSelection(path)
    selection ! Identify(path)
  }
  def receive = identify
  def identify: Receive = {
    case ActorIdentity(`path`, Some(actor)) =>
      context.setReceiveTimeout(Duration.Undefined)
      log.info("switching to active state")
      context.become(active(actor))
      context.watch(actor)
    case ActorIdentity(`path`, None) =>
      log.error(s"Remote actor with path $path is not available.")
    case ReceiveTimeout =>
      sendIdentifyRequest()
    case msg:Any =>
      log.error(s"Ignoring message $msg, not ready yet.")
  }
  def active(actor: ActorRef): Receive = {
    case Terminated(actorRef) =>
      log.info("Actor $actorRef terminated.")
      context.become(identify)
      log.info("switching to identify state")
      context.setReceiveTimeout(3 seconds)
      sendIdentifyRequest()
    case msg:Any => actor forward msg
  }
}