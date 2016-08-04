package GoTicks.com

import akka.actor._
import com.typesafe.config._

object server extends App {
  val config = ConfigFactory.load()

  val backed = ActorSystem("backed", config.getConfig("remote-backend"))

}
