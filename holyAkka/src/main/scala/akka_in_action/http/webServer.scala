
package akka_in_action.http

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn

object webServer extends App {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val route =
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Hello, holy crapper.</h2>"))
      }
    }

  val host = "127.0.0.1"
  val port = 8080
  val bindingFuture = Http().bindAndHandle(route, host, port)

  println(s"Server onlien at http://${host}:${port}/\nPress RETURN to stop...")
  StdIn.readLine
  bindingFuture
    .flatMap(_.unbind)
    .onComplete(_ => system.terminate)
}
