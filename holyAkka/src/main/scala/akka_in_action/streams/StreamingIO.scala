
package akka_in_action.streams

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.scaladsl._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object StreamingIO extends App {
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
