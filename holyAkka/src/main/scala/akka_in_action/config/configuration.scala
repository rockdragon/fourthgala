package akka_in_action.config

import com.typesafe.config.ConfigFactory

object configuration extends App {
  val configuration = ConfigFactory.load("combined")
  val subApplACfg = configuration.getConfig("subApplA")
  val config = subApplACfg.withFallback(configuration)

  println(subApplACfg)
}
