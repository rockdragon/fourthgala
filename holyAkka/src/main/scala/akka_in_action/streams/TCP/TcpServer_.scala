
package akka_in_action.streams.TCP

import java.net.InetSocketAddress
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.io.{IO, Tcp}
import Tcp._
import akka.stream.ActorMaterializer
import akka.util.ByteString

object TcpServer_ extends App {

  case object Ack extends Event

  var connections = Map.empty[String, ActorRef]

  def send(msg: String): Unit = {
    for (c <- connections) {
      c._2 ! msg
    }
  }

  class Server(serverAddress: InetSocketAddress) extends Actor {
    override def preStart(): Unit = {
      IO(Tcp) ! Bind(self, serverAddress)
    }

    override def postRestart(thr: Throwable): Unit =
      context stop self

    def receive: Receive = {
      case Bound(localAddress) =>
        println(s"Server is bound to ${localAddress}")

      case CommandFailed(_: Bind) => context stop self

      case Connected(remote, local) =>
        val connection = sender()
        val handler = ConnectionHandler.instanceOf(connection)
        connection ! Register(handler)
        connections += connection.path.toSerializationFormat -> handler
    }
  }

  object ConnectionHandler {
    def instanceOf(connection: ActorRef) =
      system.actorOf(Props(classOf[ConnectionHandler], connection))
  }

  class ConnectionHandler(connection: ActorRef) extends Actor {

    def receive: Receive = {
      case Received(data) =>
        println(data.utf8String)

      case PeerClosed =>
        context stop self

      case msg: String =>
        connection ! Write(ByteString(
          s"${msg} to: ${connection.path.toSerializationFormat}"), Ack)

      case Ack =>
        println("ACK")

      case CommandFailed(Write(_, Ack)) =>
        println("ACK failed")
        connection ! ResumeWriting
    }
  }

  // 调用部分
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val serverAddress = new InetSocketAddress("localhost", 8000)
  val server = system.actorOf(Props(classOf[Server], serverAddress))

  Thread.sleep(5000)
  send("hi")
  Thread.sleep(5000)
  send("holy")
}
