package akka_in_action.http

import spray.http._
import spray.client.pipelining._
import akka.actor.ActorSystem

import scala.concurrent.Future

object client extends App {
  implicit val system = ActorSystem()
  import system.dispatcher

  val pipeline: HttpRequest => Future[HttpResponse] = sendReceive

  val response:Future[HttpResponse] = pipeline(Get("http://www.moye.me/"))

  response.foreach { response =>
    println(response)
    system.shutdown()
  }
}
