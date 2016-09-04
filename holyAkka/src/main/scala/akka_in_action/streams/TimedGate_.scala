
package akka_in_action.streams

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.stage._
import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration._

object TimedGate_ extends App {
  // 定时器流控
  class TimedGate[A](silencePeriod: FiniteDuration)
    extends GraphStage[FlowShape[A, List[A]]] {

    val in = Inlet[A]("TimedGate.In")
    val out = Outlet[List[A]]("TimedGate.Out")

    override val shape = FlowShape.of(in, out)

    override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
      new TimerGraphStageLogic(shape) {
        var open = false
        var buffer = ListBuffer[A]()

        setHandler(in, new InHandler {
          @scala.throws[Exception](classOf[Exception])
          override def onPush(): Unit = {
            val elem = grab(in)
            buffer += elem
            if (open) {
              pull(in)
            }
            else {
              push(out, buffer.toList)
              buffer = ListBuffer[A]()
              open = true
              scheduleOnce(None, silencePeriod)
            }
          }

          override def onUpstreamFinish(): Unit = {
            if (buffer.nonEmpty) {
              emit(out, buffer.toList)
            }
            complete(out)
          }
        })

        setHandler(out, new OutHandler {
          @scala.throws[Exception](classOf[Exception])
          override def onPull(): Unit = {
            pull(in)
          }
        })

        override def onTimer(timerKey: Any): Unit = {
          open = false
        }
      }
  }

  // 调用部分
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val result = Source.fromIterator(() => Iterator from 1)
    .take(50000)
    .via(new TimedGate[Int](1 millis))
    .runWith(Sink.seq)

  val seqs = Await.result(result, 10 seconds)
  seqs.foreach(seq => println(seq.length))

  system.terminate()
}
