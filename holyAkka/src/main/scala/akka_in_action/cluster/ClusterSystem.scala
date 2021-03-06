package akka_in_action.cluster

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.typesafe.config.{Config, ConfigFactory}

object ClusterSystem extends App {
  def loadCommonConfig: Config = ConfigFactory.load("cluster")

  def remoteConfig(hostname: String, port: Int, commonConfig: Config, isMaster: Boolean = false): Config = {
    //    ConfigFactory.invalidateCaches()
    val roles = if (isMaster)
      """["seed", "master"]""".stripMargin
    else
      """["seed"]""".stripMargin
    val remoteConfigString =
      s"""
         akka.remote.netty.tcp.hostname = $hostname
         akka.remote.netty.tcp.port = $port
         akka.cluster.roles = $roles
       """.stripMargin
    println(remoteConfigString)
    ConfigFactory.parseString(remoteConfigString).withFallback(commonConfig)
  }

  def startSeedNode(hostname: String, port: Int, commonConfig: Config, isMaster: Boolean = false): ActorSystem = {
    val seedConfig = remoteConfig(hostname, port, commonConfig, isMaster)
    ActorSystem("words", seedConfig)
  }

  def leaveCluster(system: ActorSystem): Unit = {
    val cluster = Cluster(system)
    cluster.leave(cluster.selfAddress)
  }

  val commonConfig = loadCommonConfig

  val system2551 = startSeedNode("127.0.0.1", 2551, commonConfig, true)
  val listener = system2551.actorOf(Props[ClusterDomainEventListener], "listener")

  Thread.sleep(1000)

  val system2552 = startSeedNode("127.0.0.1", 2552, commonConfig)
  val system2553 = startSeedNode("127.0.0.1", 2553, commonConfig)

  Thread.sleep(10000)

  1 to 6 foreach { _ => println("[DONE] ------------------->>> ") }

  leaveCluster(system2551)
}
