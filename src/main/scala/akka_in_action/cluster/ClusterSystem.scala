package akka_in_action.cluster

import akka.actor.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}

object ClusterSystem extends App {
  def loadCommonConfig: Config = ConfigFactory.load("cluster")

  def remoteConfig(hostname: String, port: Int, commonConfig: Config): Config = {
    ConfigFactory.invalidateCaches()
    val remoteConfigString =
      s"""
         akka.remote.netty.tcp.hostname = $hostname
         akka.remote.netty.tcp.port = $port
       """.stripMargin
    ConfigFactory.parseString(remoteConfigString).withFallback(commonConfig)
  }

  def startSeedNode(hostname: String, port: Int, commonConfig: Config): Unit = {
    val seedConfig = remoteConfig(hostname, port, commonConfig)
    val seedSystem = ActorSystem("words", seedConfig)
  }

  val commonConfig = loadCommonConfig

  startSeedNode("127.0.0.1", 2551, commonConfig)

  Thread.sleep(1000)

  startSeedNode("127.0.0.1", 2552, commonConfig)
  startSeedNode("127.0.0.1", 2553, commonConfig)
}
