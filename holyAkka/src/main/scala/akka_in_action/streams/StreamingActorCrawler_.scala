
package akka_in_action.streams

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import akka.stream.ActorMaterializer
import akka.stream.actor.{ActorSubscriber, MaxInFlightRequestStrategy}
import akka.stream.actor.ActorSubscriberMessage._
import akka.stream.scaladsl.{Sink, Source}

object StreamingActorCrawler_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  object Messages {
    case class Request(id: Int, url: String)
    case class Response(id: Int, result: String)
    case class Crawl(id: Int, url: String)
    def get(url: String): String =
      scala.io.Source.fromURL(url)("utf-8").mkString
  }

  class Worker extends Actor {
    import Messages._
    override def receive: Receive = {
      case Crawl(id, url) =>
        val result = Messages.get(url)
        sender() ! Response(id, result)
    }
  }

  class WorkerPool extends ActorSubscriber {
    import Messages._
    val MaxQueueSize = 18 // 队列大小
    var queue = Map.empty[Int, String]

    val router = {
      val routees = Vector.fill(36) { // worker数量
        ActorRefRoutee(context.actorOf(Props[Worker]))
      }
      Router(RoundRobinRoutingLogic(), routees)
    }

    override val requestStrategy =
      new MaxInFlightRequestStrategy(max = MaxQueueSize) {
        override def inFlightInternally: Int = queue.size
      }

    def receive: Receive = {
      case OnNext(Request(id, url)) =>
        assert(queue.size <= MaxQueueSize, s"queued too many: ${queue.size}")
        queue += (id -> url)
        router.route(Crawl(id, url), self) // 路由
      case OnComplete =>
        Thread.sleep(500)
        system.terminate()
      case OnError(e) =>
        println(s"error: $e")
        system.terminate()
      case Response(id, result) =>
        queue -= id
        println(s"[$id] DONE, HTML content length: ${result.length}")
    }
  }

  val N = 255 // 总任务数
  val r = Source(1 to N)
    .map(Messages.Request(_, "http://example.org"))
    .runWith(Sink.actorSubscriber(Props(new WorkerPool)))
}


