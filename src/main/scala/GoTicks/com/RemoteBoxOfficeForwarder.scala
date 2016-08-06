package GoTicks.com

import GoTicks.com.boxOffice.{RestInterface, BoxOffice, BoxOfficeCreator}
import akka.actor._

import scala.concurrent.duration._

trait ConfiguredRemoteBoxOfficeDeployment
  extends BoxOfficeCreator {
  this: Actor =>
  override def createBoxOffice = {
    context.actorOf(Props[RemoteBoxOfficeForwarder], "forwarder")
  }
}

class RemoteBoxOfficeForwarder extends Actor with ActorLogging {
  deployAndWatch()

  def deployAndWatch(): Unit = {
    val actor = context.actorOf(Props[BoxOffice], "boxOffice")
    context.watch(actor)
    log.info("switching to maybe active state")
    context.become(maybeActive(actor))
    context.setReceiveTimeout(Duration.Undefined)
  }

  def receive = deploying

  def deploying(): Receive = {
    case ReceiveTimeout =>
      deployAndWatch()
    case msg: Any =>
      log.error(s"Ignoring message $msg, not ready yet.")
  }

  def maybeActive(actor: ActorRef): Receive = {
    case Terminated(actorRef) =>
      log.info(s"Actor $actorRef terminated.")
      log.info("switching to deploying state")
      context.become(deploying)
      context.setReceiveTimeout(3 seconds)
      deployAndWatch()
    case msg: Any =>
      actor forward msg
  }
}

class RestInterfaceWatch extends RestInterface
with ConfiguredRemoteBoxOfficeDeployment {
  val system = ActorSystem("RestInterfaceWatch")
  val restInterface = system.actorOf(Props[RestInterfaceWatch]), "restInterface"
}
