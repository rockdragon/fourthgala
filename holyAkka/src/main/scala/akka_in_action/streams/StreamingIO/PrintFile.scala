
package akka_in_action.streams.StreamingIO

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, IOResult}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object PrintFile extends App {
  implicit val system = ActorSystem("actor-system")
  implicit val materializer = ActorMaterializer()

  val file = Paths.get("holyAkka/src/main/scala/akka_in_action/streams/StreamingIO.scala").toAbsolutePath()

  val foreach: Future[IOResult] = FileIO.fromPath(file)
    .to(Sink.foreach(println))
    .run()

  val result = Await.result(foreach, 500 millis)
  println(result)

  system.terminate()
}
