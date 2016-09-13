
package akka_in_action.streams.TCP

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString

object TcpClient_ extends App {

  object Client {
    def props(remote: InetSocketAddress, replies: ActorRef) =
      Props(classOf[Client], remote, replies)
  }

  class Client(remote: InetSocketAddress, listener: ActorRef) extends Actor {

    import Tcp._
    import context.system

    IO(Tcp) ! Connect(remote)

    def receive: Receive = {
      case CommandFailed(_: Connect) =>
        listener ! "connect failed"
        context stop self

      case c @ Connected(remote, local) =>
        listener ! c
        val connection = sender()
        connection ! Register(self)
        context become {
          case data: ByteString =>
            connection ! Write(data)
          case CommandFailed(w: Write) =>
            // O/S buffer was full
            listener ! "write failed"
          case Received(data) =>
            listener ! data
          case "close" =>
            connection ! Close
          case _: ConnectionClosed =>
            listener ! "connection closed"
            context stop self
        }
    }
  }
}
