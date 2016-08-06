package GoTicks.com

import GoTicks.com.boxOffice.RestInterface
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import spray.can.Http
import spray.can.Http.Bind

object BackendRemoteDeployMain extends App {
  val config = ConfigFactory.load("backend")
  val system = ActorSystem("backend", config)
}

object FrontendRemoteDeployMain extends App {
  val config = ConfigFactory.load("frontend-remote-deploy")
  val host = config.getString("http.host")
  val port = config.getInt("http.port")
  val system = ActorSystem("frontend", config)

  val restInterface = system.actorOf(Props[RestInterface],
    "restInterface")
  Http(system).manager ! Bind(listener = restInterface,
    interface = host, port = port)
}
