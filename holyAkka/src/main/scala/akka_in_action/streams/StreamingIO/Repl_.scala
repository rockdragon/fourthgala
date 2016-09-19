
package akka_in_action.streams.StreamingIO

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Framing, Source, Flow, Tcp}
import akka.util.ByteString

object Repl_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val connection = Tcp().outgoingConnection("127.0.0.1", 8888)

  val replParser =
    Flow[String].takeWhile(_ != "q")
      .concat(Source.single("BYE"))
      .map(elem => ByteString(s"$elem\n"))

  val repl = Flow[ByteString]
    .via(Framing.delimiter(
      ByteString("\n"),
      maximumFrameLength = 256,
      allowTruncation = true
    ))
  .map(_.utf8String)
  .map(text => println("Server: "+text))
  .map(_ => readLine("> "))
  .via(replParser)

  connection.join(repl).run()

}
