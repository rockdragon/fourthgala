
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka_in_action.streams.DuplicatorShape_.Duplicator

import scala.concurrent.Await
import scala.concurrent.duration._

object SourceFlowDuplicators_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  implicit class SourceDuplicator[Out, Mat](s: Source[Out, Mat]) {
    def duplicateElements: Source[Out, Mat] = s.via(new Duplicator)
  }

  val f =  Source(1 to 3).duplicateElements.runWith(Sink.seq)
  val r = Await.result(f, 300 millis)
  println(r)

  implicit class FlowDuplicator[In, Out, Mat](s: Flow[In, Out, Mat]) {
    def duplicateElements: Flow[In, Out, Mat] = s.via(new Duplicator)
  }

  val f2 = Source(1 to 3).via(Flow[Int].duplicateElements).runWith(Sink.seq)
  val r2 = Await.result(f2, 300 millis)
  println(r2)

  system.terminate()
}
