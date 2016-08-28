
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source}

import scala.concurrent.Await
import scala.concurrent.duration._

object GraphBuilder extends App {
  implicit val system = ActorSystem("actor-system")
  implicit val materializer = ActorMaterializer()

  val topHeadSink = Sink.head[Int]
  val bottomHeadSink = Sink.head[Int]
  val sharedDoubler = Flow[Int].map(_ * 2)

  val results = RunnableGraph.fromGraph(GraphDSL.create(topHeadSink, bottomHeadSink)((_, _)) { implicit builder =>
    (topHS, bottomHS) =>
      import GraphDSL.Implicits._
      val broadcast = builder.add(Broadcast[Int](2))
      Source.fromIterator(() => Iterator from 2) ~> broadcast.in

      broadcast.out(0) ~> sharedDoubler ~> topHS.in
      broadcast.out(1) ~> sharedDoubler ~> bottomHS.in

      ClosedShape
  }).run()

  val r1 = Await.result(results._1, 300 millis)
  val r2 = Await.result(results._2, 300 millis)

  println(r1, r2)
  system.terminate()
}
