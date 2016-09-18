
package PrintFile

import akka_in_action.streams.StreamingIO.PrintFile

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Tcp.{ServerBinding, IncomingConnection}
import akka.stream.scaladsl.{Flow, Tcp, Source, Framing}
import akka.util.ByteString

import scala.concurrent.Future

object Echo_ extends App {
  // 调用部分
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val host = "127.0.0.1"
  val port = 8080

  //scalastyle:off
  val connections: Source[IncomingConnection, Future[ServerBinding]] =
    Tcp().bind(host, port)
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
