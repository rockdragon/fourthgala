
package akka_in_action.streams.TCP

import java.net.InetSocketAddress
import akka.actor.{Actor, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString

object TcpServer_ extends App {

  class SimplisticHandler extends Actor {
    import Tcp._
    def receive: Receive = {
      case Received(data) => sender() ! Write(data)
      case PeerClosed     => context stop self
    }
  }

  class Server extends Actor {
    import Tcp._
    import context.system

    IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 0))

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
}
