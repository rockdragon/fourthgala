
package akka_in_action.streams.TCP

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import akka.stream.ActorMaterializer
import akka.util.ByteString

object TcpClient_ extends App {

  class Client(remote: InetSocketAddress, listener: ActorRef) extends Actor {
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

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  def work(sender: ActorRef): Unit = {
    sender ! ByteString("hello")
  }

  class Listener extends Actor {
    def receive: Receive = {
      case c @ Connected(remote, local) =>
        println(s"Connnected -> remote: $remote, local: $local")
        work(sender())

      case f @ "connect failed" =>
        println(f)
        system.terminate()
    }
  }

  val listener = system.actorOf(Props[Listener])
  val serverAddress = new InetSocketAddress("localhost", 8000)
  val client = system.actorOf(Props(classOf[Client], serverAddress, listener))
}
