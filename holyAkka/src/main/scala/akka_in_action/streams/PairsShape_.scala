
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, SourceShape}
import akka.stream.scaladsl.{GraphDSL, Sink, Source, Zip}
import GraphDSL.Implicits._
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object PairsShape_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val pairs = Source.fromGraph(GraphDSL.create() { implicit builder =>
    val zip = builder.add(Zip[Int, Int]())

    def ints = Source.fromIterator(() => Iterator from(1))

    ints.filter(_ % 2 != 0) ~> zip.in0
    ints.filter(_ % 2 == 0) ~> zip.in1

    SourceShape(zip.out)
  })

  val firstPair: Future[(Int, Int)] = pairs.runWith(Sink.head)
  val r = Await.result(firstPair, 300 millis)
  println(r)

  system.terminate()
}
