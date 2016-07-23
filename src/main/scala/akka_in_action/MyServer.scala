package akka_in_action.MyServer

import akka.actor.{Props, ActorSystem, Actor}
import akka.io.IO
import spray.can.Http
import spray.routing.HttpService
import spray.http.MediaTypes._

trait MyService extends HttpService {
  val myRoute = {
    path("something") {
      respondWithMediaType(`text/plain`) {
        complete("okay")
      }
    }
    path("hello") {
      respondWithMediaType(`text/plain`) {
        complete("kitty")
      }
    }
  }
}

class MyServiceActor extends Actor with MyService {
  def actorRefFactory = context
  def receive = runRoute(myRoute)
}

object server extends App {
  try {
    implicit val system = ActorSystem("my-system")
    val handler = system.actorOf(Props[MyServiceActor], name = "my-service")

    IO(Http) ! Http.Bind(handler, interface = "localhost", port = 8080)
  } catch {
    case e => println("error: ", e)
  }
}
