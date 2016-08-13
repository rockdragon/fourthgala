package akka_in_action.cluster

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object SeedNodes extends App{
  val seedConfig = ConfigFactory.load("cluster")
  val seedSystem = ActorSystem("words", seedConfig)


}
