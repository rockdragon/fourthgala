
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, UniformFanInShape, ClosedShape}
import akka.stream.scaladsl._
import scala.concurrent.duration._
import scala.concurrent.{Future, Await}

object MergeGraph extends App {
  implicit val system = ActorSystem("actor-system")
  implicit val materilizer = ActorMaterializer()

  val pickMaxOfThree = GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val zip1 = b.add(ZipWith[Int, Int, Int](math.max _))
    val zip2 = b.add(ZipWith[Int, Int, Int](math.max _))
    zip1.out ~> zip2.in0

    UniformFanInShape(zip2.out, zip1.in0, zip1.in1, zip2.in1)
  }

  val resultSink = Sink.head[Int]

  val g = RunnableGraph.fromGraph(GraphDSL.create(resultSink) { implicit b => sink =>
    import GraphDSL.Implicits._

    val pm3 = b.add(pickMaxOfThree)

    Source.single(1) ~> pm3.in(0)
    Source.single(2) ~> pm3.in(1)
    Source.single(3) ~> pm3.in(2)

    pm3.out ~> sink.in
    ClosedShape
  })

  val max: Future[Int] = g.run()
  val r = Await.result(max, 300 millis)
  println(r)
}
