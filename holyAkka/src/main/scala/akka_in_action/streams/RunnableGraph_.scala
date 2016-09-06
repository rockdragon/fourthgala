
package akka_in_action.streams


import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.stream.stage.{OutHandler, InHandler, GraphStageLogic, GraphStageWithMaterializedValue}

import scala.concurrent.{Promise, Future, Await}
import scala.concurrent.duration._
import GraphDSL.Implicits._

object RunnableGraph_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val r = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder =>
    val B = builder.add(Broadcast[Int](2))
    val C = builder.add(Merge[Int](2))
    val E = builder.add(Balance[Int](2))
    val F = builder.add(Merge[Int](2))

    Source.single(0) ~> B.in; B.out(0) ~> C.in(1); C.out ~> F.in(0)
    C.in(0) <~ F.out

    B.out(1).map(_ + 1) ~> E.in; E.out(0) ~> F.in(1)
    E.out(1) ~> Sink.foreach(println)

    ClosedShape
  })

  val f = r.run()

  system.terminate()
}
