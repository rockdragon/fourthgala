package GoTicks.com

import GoTicks.com.boxOffice.BoxOfficeCreator
import akka.actor.{Props, Actor}
import com.typesafe.config.ConfigFactory

object RemoteBoxOfficeCreator {
  val config = ConfigFactory.load("frontend").getConfig("backend")
  val host = config.getString("host")
  val port = config.getInt("port")
  val protocol = config.getString("protocol")
  val systemName = config.getString("system")
  val actorName = config.getString("actor")
}

trait RemoteBoxOfficeCreator extends BoxOfficeCreator {
  this: Actor =>

  import RemoteBoxOfficeCreator._

  def createPath: String = {
    s"$protocol://$systemName@$host:$port/$actorName"
  }

  override def createBoxOffice = {
    val path = createPath
    context.actorOf(Props(classOf[RemoteLookup], path),
      "lookupBoxOffice")
  }
}
