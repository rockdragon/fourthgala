
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object Materialization extends App {
  implicit val system = ActorSystem("actor-system")
  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  val source = Source(1 to 10)
  val sink = Sink.fold[Int, Int](0)(_ + _)

  val runnable: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)

  val sum: Future[Int] = runnable.run()


  sum onComplete {
    case Success(x) => println(x)
    case Failure(e) => println(e)
  }

  sum andThen{
    case _ => system terminate()
  }
}
