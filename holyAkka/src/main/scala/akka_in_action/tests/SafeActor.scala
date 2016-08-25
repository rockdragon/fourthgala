
package akka_in_action.tests

import akka.actor.{Actor, Props}

object SafeActor {

  object DemoActor {
    def props(magicNumber: Int): Props = Props(new DemoActor(magicNumber))
  }

  class DemoActor(magicNumber: Int) extends Actor {
    def receive = {
      case x: Int => sender() ! (x + magicNumber)
    }
  }

  class SomeOtherActor extends Actor {
    // Props(new DemoActor(42)) would not be safe
    context.actorOf(DemoActor.props(42), "demo")

    def receive = ???
  }

}