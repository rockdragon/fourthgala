package akka_in_action.cluster

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object ClusterSystem extends App{
  def startSeedNode(configName: String): Unit = {
    val seedConfig = ConfigFactory.load(configName)
    val seedSystem = ActorSystem("words", seedConfig)
  }

  startSeedNode("seed1")

  Thread.sleep(1000)

  startSeedNode("seed2")
  startSeedNode("seed3")
}
