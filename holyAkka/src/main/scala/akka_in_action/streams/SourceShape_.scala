
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, SourceShape}
import akka.stream.scaladsl.{Sink, Zip, GraphDSL, Source}

import scala.concurrent.Await
import scala.concurrent.duration._

object SourceShape_ extends App {
  implicit val system = ActorSystem("actor-system")
  implicit val materializer = ActorMaterializer()

  val pairs = Source.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val zip = b.add(Zip[Int, Int]())
    def ints = Source.fromIterator(() => Iterator.from(1))

    ints.filter(_ % 2 == 0) ~> zip.in0
    ints.filter(_ % 2 == 1) ~> zip.in1

    SourceShape(zip.out)
  })

  val firstPair = pairs.runWith(Sink.head)
  val result = Await.result(firstPair, 300 millis)
  println(result)

  system.terminate
}
