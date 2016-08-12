package akka_in_action.FSM

import akka.actor.ActorSystem
import akka.agent.Agent

object AgentExample2 extends App {
  implicit val system = ActorSystem("actor-system")
  import system.dispatcher

  val agent1 = Agent(2)

  // applicative
  val agent2 = agent1 map (_ + 1)
  println(agent2.get)

  // monad
  val agent3 = agent2 flatMap(x => Agent(x + 3))
  println(agent3.get)

  system shutdown
}
