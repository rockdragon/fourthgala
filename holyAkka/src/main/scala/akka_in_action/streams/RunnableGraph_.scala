
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

  import GraphDSL.Implicits._
  val partial = GraphDSL.create() { implicit builder =>
    val B = builder.add(Broadcast[Int](2))
    val C = builder.add(Merge[Int](2))
    val E = builder.add(Balance[Int](2))
    val F = builder.add(Merge[Int](2))

    C  <~  F
    B  ~>                            C  ~>  F
    B  ~>  Flow[Int].map(_ + 1)  ~>  E  ~>  F
    FlowShape(B.in, E.out(1))
  }.named("partial")

  val result = Source.repeat(101)
    .via(Flow.fromGraph(partial))
    .toMat(Sink.head)(Keep.right)
    .run()

  val r = Await.result(result, 300 millis)
  println(r)

  system.terminate()
}
