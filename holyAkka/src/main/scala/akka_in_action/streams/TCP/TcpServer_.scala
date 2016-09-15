
package akka_in_action.streams.TCP

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.io.{IO, Tcp}
import Tcp._
import akka.stream.ActorMaterializer
import akka.util.ByteString

object TcpServer_ extends App {

  class Server(serverAddress: InetSocketAddress) extends Actor {
    var connections = Map.empty[String, ActorRef]

    IO(Tcp) ! Bind(self, serverAddress)

    def receive: Receive = {
      case b@Bound(localAddress) =>
        println("Server is ready...")

      case CommandFailed(_: Bind) => context stop self

      case c@Connected(remote, local) =>
        val connection = sender()
        val handler = ConnectionHandler.instance
        connection ! Register(handler)
        connections += connection.path.toSerializationFormat -> connection

        if (connections.keys.size > 1)
            for (c <- connections) c._2 ! Write(ByteString(s"hello, ${c._1}"))
    }
  }

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  object ConnectionHandler {
    def instance = system.actorOf(Props[ConnectionHandler])
  }

  class ConnectionHandler extends Actor {
    var connections = Vector.empty[ActorRef]

    def receive: Receive = {
      case Received(data) =>
        println(data.utf8String)
      case PeerClosed =>
        context stop self
    }
  }

  val serverAddress = new InetSocketAddress("localhost", 8000)
  val server = system.actorOf(Props(classOf[Server], serverAddress))
}
