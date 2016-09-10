
package akka_in_action.streams

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import akka.stream.ActorMaterializer
import akka.stream.actor.{ActorSubscriber, MaxInFlightRequestStrategy}
import akka.stream.actor.ActorSubscriberMessage._
import akka.stream.scaladsl.{Sink, Source}

object ActorSubscriber_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  class Worker extends Actor {
    import WorkerPool._
    def receive: Receive = {
      case Work(id) =>
        println(s"worker: $id")
        sender() ! Reply(id)
    }
  }

  object WorkerPool {
    case class Msg(id: Int, replyTo: ActorRef)
    case class Work(id: Int)
    case class Reply(id: Int)
    case class Done(id: Int)

    def props: Props = Props(new WorkerPool)
  }

  class WorkerPool extends ActorSubscriber {
    import WorkerPool._
    val MaxQueueSize = 18
    var queue = Map.empty[Int, ActorRef]

    val router = {
      val routees = Vector.fill(3) {
        ActorRefRoutee(context.actorOf(Props[Worker]))
      }
      Router(RoundRobinRoutingLogic(), routees)
    }

    override val requestStrategy = new MaxInFlightRequestStrategy(max = MaxQueueSize) {
      override def inFlightInternally: Int = queue.size
    }

    def receive: Receive = {
      case OnNext(Msg(id, replyTo)) =>
        queue += (id -> replyTo)
        assert(queue.size <= MaxQueueSize, s"queued too many: ${queue.size}")
        router.route(Work(id), self)
      case Reply(id) =>
        queue(id) ! Done(id)
        queue -= id
        println(s"reply: $id")
    }
  }

  val replyTo = system.actorOf(Props[Worker])
  val N = 117
  val r = Source(1 to N).map(WorkerPool.Msg(_, replyTo))
    .runWith(Sink.actorSubscriber(WorkerPool.props))

  Thread.sleep(2000)

  system.terminate()
}
