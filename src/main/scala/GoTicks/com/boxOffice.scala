package GoTicks.com

import akka.actor.Actor.Receive
import akka.actor._
import com.typesafe.config.ConfigFactory
import spray.routing.{HttpService, HttpServiceActor}

object boxOffice {

  class BoxOffice extends Actor {
    def receive = {
      case _ =>
    }
  }

  trait BoxOfficeCreator {
    this: Actor =>
    def createBoxOffice: ActorRef = {
      context.actorOf(Props[BoxOffice], "boxOffice")
    }
  }

  trait RestApi extends HttpService with ActorLogging with BoxOfficeCreator {
    actor: Actor =>
    val boxOffice = createBoxOffice
  }

  class RestInterface extends HttpServiceActor with RestApi {
    override def receive: Receive = ???
  }

  val config = ConfigFactory.load()
  val system = ActorSystem("singlenode", config.getConfig("remote-single"))

  val restInterface = system.actorOf(Props[RestInterface], "restInterface")

}
