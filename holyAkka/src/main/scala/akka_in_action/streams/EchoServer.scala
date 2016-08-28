
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Tcp.{IncomingConnection, ServerBinding}
import akka.stream.scaladsl.{Flow, Framing, Sink, Source, Tcp}
import akka.util.ByteString

import scala.concurrent.Future

object EchoServer extends App {
  implicit val system = ActorSystem("actor-system")

  import system.dispatcher

  implicit val materializer = ActorMaterializer()

  val connections: Source[IncomingConnection, Future[ServerBinding]] =
    Tcp().bind("127.0.0.1", 3000)
  connections runForeach { connection =>
    println(s"New connection from: ${connection.remoteAddress}")

    val echo = Flow[ByteString]
      .via(Framing.delimiter(
        ByteString("\n"),
        maximumFrameLength = 256,
        allowTruncation = true))
      .map(_.utf8String)
      .map(_ + "!!!\n")
      .map(ByteString(_))

    connection.handleWith(echo)
  }
}
