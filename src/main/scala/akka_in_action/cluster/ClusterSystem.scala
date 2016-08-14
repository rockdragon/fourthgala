package akka_in_action.cluster

import akka.actor.{ActorSystem}
import akka.cluster.Cluster
import com.typesafe.config.{Config, ConfigFactory}

object ClusterSystem extends App {
  def loadCommonConfig: Config = ConfigFactory.load("cluster")

  def remoteConfig(hostname: String, port: Int, commonConfig: Config): Config = {
//    ConfigFactory.invalidateCaches()
    val remoteConfigString =
      s"""
         akka.remote.netty.tcp.hostname = $hostname
         akka.remote.netty.tcp.port = $port
       """.stripMargin
    ConfigFactory.parseString(remoteConfigString).withFallback(commonConfig)
  }

  def startSeedNode(hostname: String, port: Int, commonConfig: Config): ActorSystem = {
    val seedConfig = remoteConfig(hostname, port, commonConfig)
    ActorSystem("words", seedConfig)
  }

  def leaveCluster(system: ActorSystem): Unit = {
    val cluster = Cluster(system)
    cluster.leave(cluster.selfAddress)
  }

  val commonConfig = loadCommonConfig

  val system2551 = startSeedNode("127.0.0.1", 2551, commonConfig)

  Thread.sleep(1000)

  val system2552 = startSeedNode("127.0.0.1", 2552, commonConfig)
  val system2553 = startSeedNode("127.0.0.1", 2553, commonConfig)

  Thread.sleep(10000)

  1 to 6 foreach { _ => println("[DONE] ------------------->>> ") }

  leaveCluster(system2551)
}
