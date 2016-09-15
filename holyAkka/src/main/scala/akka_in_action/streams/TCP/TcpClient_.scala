
package akka_in_action.streams.TCP

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import akka.stream.ActorMaterializer
import akka.util.ByteString

object TcpClient_ extends App {

  class Client(remote: InetSocketAddress, proxy: ActorRef) extends Actor {
    IO(Tcp) ! Connect(remote)

    def receive: Receive = {
      case CommandFailed(_: Connect) =>
        proxy ! "connect failed"
        context stop self

      case c @ Connected(remote, local) =>
        proxy ! c
        val connection = sender()
        connection ! Register(self)
        context become {
          case data: ByteString =>
            connection ! Write(data)
          case CommandFailed(w: Write) =>
            // O/S buffer was full
            proxy ! "write failed"
          case Received(data) =>
            proxy ! data
          case "close" =>
            connection ! Close
          case _: ConnectionClosed =>
            proxy ! "connection closed"
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

  class ClientProxy extends Actor {
    def receive: Receive = {
      case c @ Connected(remote, local) =>
        println(s"Connnected -> remote: $remote, local: $local")
        work(sender())

      case b: ByteString => println(b.utf8String)

      case f @ ("connect failed" | "connection closed" | "write failed") =>
        println(f)
    }
  }

  val proxy = system.actorOf(Props[ClientProxy])
  val proxy1 = system.actorOf(Props[ClientProxy])
  val serverAddress = new InetSocketAddress("localhost", 8000)

  val client = system.actorOf(Props(classOf[Client], serverAddress, proxy))
  Thread.sleep(1000)
  val client2 = system.actorOf(Props(classOf[Client], serverAddress, proxy1))
}
