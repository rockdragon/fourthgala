package akka_in_action.GoTicks

import scala.concurrent.duration._
import akka.actor._

class RemoteLookup(path:String) extends Actor with ActorLogging {
  // actor will receive a ReceiveTimeout message which in inactive 3 seconds
  context.setReceiveTimeout(3 seconds)
  sendIdentifyRequest()

  def sendIdentifyRequest(): Unit = {
    val selection = context.actorSelection(path)
    // A message all Actors will understand,
    // that when processed will reply with akka.actor.ActorIdentity containing the ActorRef.
    selection ! Identify(path)
  }

  def receive = identify
  def identify: Receive = {
    // literal identifier
    case ActorIdentity(`path`, Some(actor)) =>
      // switch off ReceiveTimeout checking feature
      context.setReceiveTimeout(Duration.Undefined)
      log.info("switching to active state")
      // Changes the Actor's behavior to become the new 'Receive'
      context.become(active(actor))
      // Registers this actor as a Monitor for the provided ActorRef.
      context.watch(actor)
    case ActorIdentity(`path`, None) =>
      log.error(s"Remote actor with path $path is not available.")
    case ReceiveTimeout =>
      sendIdentifyRequest()
    case msg:Any =>
      log.error(s"Ignoring message $msg, not ready yet.")
  }
  def active(actor: ActorRef): Receive = {
    // Message that is sent to a watching actor when the watched actor terminates.
    case Terminated(actorRef) =>
      log.info("Actor $actorRef terminated.")
      context.become(identify)
      log.info("switching to identify state")
      context.setReceiveTimeout(3 seconds)
      sendIdentifyRequest()
    case msg:Any => actor forward msg // Forwards the message and passes the original sender actor as the sender.
  }
}