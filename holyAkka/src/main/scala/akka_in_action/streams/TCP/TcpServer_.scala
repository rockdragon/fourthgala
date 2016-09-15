
package akka_in_action.streams.TCP

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.{IO, Tcp}
import Tcp._
import akka.stream.ActorMaterializer

object TcpServer_ extends App {

  class SimplisticHandler extends Actor {
    import Tcp._
    def receive: Receive = {
      case Received(data) =>
        sender() ! Write(data)
        println(data.utf8String)
      case PeerClosed     =>
        context stop self
    }
  }

  class Server(port: Int) extends Actor {

    IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", port))

    def receive: Receive = {
      case b @ Bound(localAddress) =>
      // do some logging or setup ...

      case CommandFailed(_: Bind) => context stop self

      case c @ Connected(remote, local) =>
        val handler = context.actorOf(Props[SimplisticHandler])
        val connection = sender()
        connection ! Register(handler)
    }
  }

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val server = system.actorOf(Props(classOf[Server], 8000))
}
