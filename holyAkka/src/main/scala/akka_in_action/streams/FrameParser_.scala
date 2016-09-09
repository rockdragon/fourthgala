
package akka_in_action.streams

import java.nio.ByteOrder

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.stream.stage.{InHandler, OutHandler, GraphStageLogic, GraphStage}
import akka.util.ByteString

import scala.concurrent.Await
import scala.concurrent.duration._

object FrameParser_ extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val framing = BidiFlow.fromGraph(GraphDSL.create() { implicit b =>
    implicit val order = ByteOrder.LITTLE_ENDIAN

    def addLengthHeader(bytes: ByteString) = {
      val len = bytes.length
      ByteString.newBuilder.putInt(len).append(bytes).result()
    }

    class FrameParser extends GraphStage[FlowShape[ByteString, ByteString]] {
      val in = Inlet[ByteString]("FrameParser.in")
      val out = Outlet[ByteString]("FrameParser.out")

      override val shape = FlowShape.of(in, out)

      override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
        new GraphStageLogic(shape) {
          var stash = ByteString.empty
          var needed = -1

          setHandler(out, new OutHandler {
            @throws[Exception](classOf[Exception])
            override def onPull(): Unit = {
              if (isClosed(in)) run()
              else pull(in)
            }
          })

          setHandler(in, new InHandler {
            @throws[Exception](classOf[Exception])
            override def onPush(): Unit = {
              val bytes = grab(in)
              stash = stash ++ bytes
              run()
            }

            @throws[Exception](classOf[Exception])
            override def onUpstreamFinish(): Unit = {
              if (stash.isEmpty) completeStage()
              else if (isAvailable(out)) run()
            }
          })

          private def run(): Unit = {
            if (needed == -1) {
              if (stash.length < 4) {
                if (isClosed(in)) completeStage()
                else pull(in)
              } else {
                needed = stash.iterator.getInt
                stash = stash.drop(4)
                run()
              }
            } else if(stash.length < needed) {
              if(isClosed(in)) completeStage()
              else pull(in)
            } else {
              val emit = stash.take(needed)
              stash = stash.drop(needed)
              needed = -1
              push(out, emit)
            }
          }
        }
    }

    val outbound = b.add(Flow[ByteString].map(addLengthHeader))
    val inbound = b.add(Flow[ByteString].via(new FrameParser))
    BidiShape.fromFlows(outbound, inbound)
  })

  trait Message
  case class Ping(id: Int) extends Message
  case class Pong(id: Int) extends Message

  def toBytes(msg: Message): ByteString = {
    implicit val order = ByteOrder.LITTLE_ENDIAN
    msg match {
      case Ping(id) => ByteString.newBuilder.putByte(1).putInt(id).result()
      case Pong(id) => ByteString.newBuilder.putByte(2).putInt(id).result()
    }
  }

  def fromBytes(bytes: ByteString): Message = {
    implicit val order = ByteOrder.LITTLE_ENDIAN
    val it = bytes.iterator
    it.getByte match {
      case 1     => Ping(it.getInt)
      case 2     => Pong(it.getInt)
      case other => throw new RuntimeException(s"parse error: expected 1|2 got $other")
    }
  }

  val codec = BidiFlow.fromFunctions(toBytes _, fromBytes _)
  /* construct protocol stack
 *         +------------------------------------+
 *         | stack                              |
 *         |                                    |
 *         |  +-------+            +---------+  |
 *    ~>   O~~o       |     ~>     |         o~~O    ~>
 * Message |  | codec | ByteString | framing |  | ByteString
 *    <~   O~~o       |     <~     |         o~~O    <~
 *         |  +-------+            +---------+  |
 *         +------------------------------------+
 */
  val stack = codec.atop(framing)
  val pingpong = Flow[Message].collect { case Ping(id) => Pong(id) }
  val flow = stack.atop(stack.reversed).join(pingpong)
  val result = Source((0 to 9).map(Ping)).via(flow).limit(20).runWith(Sink.seq)
  val r = Await.result(result, 1.second)
  println(r)

  system.terminate()
}
