
package akka_in_action.streams

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import akka.stream.ActorMaterializer
import akka.stream.actor.{ActorPublisher, ActorSubscriber, MaxInFlightRequestStrategy}
import akka.stream.actor.ActorSubscriberMessage._
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.actor.ActorPublisherMessage._

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration._

object ActorPublisher_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  object JobManager {
    def props: Props = Props[JobManager]

    final case class Job(payload: String)
    case object JobAccepted
    case object JobDenied
  }

  class JobManager extends ActorPublisher[JobManager.Job] {
    import JobManager._

    val MaxBufferSize = 18
    var buf = Vector.empty[Job]

    def receive: Receive = {
      case job: Job if buf.size == MaxBufferSize =>
        sender() ! JobDenied
      case job: Job =>
        sender() ! JobAccepted
        if (buf.isEmpty && totalDemand > 0) {
          onNext(job)
        } else {
          buf :+= job
          deliverJob()
        }
      case Request(_) =>
        deliverJob()
      case Cancel =>
        context.stop(self)
    }

    @tailrec final def deliverJob(): Unit =
      if (totalDemand > 0) {
        if(totalDemand <= Int.MaxValue) {
          val (use, keep) = buf.splitAt(totalDemand.toInt)
          buf = keep
          use foreach onNext
        } else {
          val (use, keep) = buf.splitAt(Int.MaxValue)
          buf = keep
          use foreach onNext
          deliverJob()
        }
      }
  }

  system.terminate()
}
