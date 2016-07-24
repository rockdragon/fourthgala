package akka_in_action.tests

import akka.actor.ActorSystem
import akka.testkit.{TestKit}
import org.scalatest.{MustMatchers, WordSpec}

class SilentActor01Test extends TestKit(ActorSystem("testsystem"))
with WordSpec
with MustMatchers
with StopSystemAfterAll {

  "A Silent Actor" must {
    "change state when it receives a message, single threaded" in {
      fail("not implemented yet")
    }
    "change state when it receives a message, multi-threaded" in {
      fail("not implemeted yet")
    }
  }

}
