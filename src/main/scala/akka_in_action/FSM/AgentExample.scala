package akka_in_action.FSM

import akka.actor.ActorSystem
import akka.agent.Agent
import scala.concurrent.duration._

case class BookStatics(val nameBook: String, nrSold: Int)

case class StateBookStatics(val sequence: Long,
                            books: Map[String, BookStatics])

object AgentSample extends App {
  implicit val system = ActorSystem("actor-system")

  import system.dispatcher

  val stateAgent = Agent(StateBookStatics(0, Map()))

  val newState = StateBookStatics(1, Map("book" -> BookStatics("book", 1)))
  stateAgent send newState

  println("1.", stateAgent.get)

  Thread.sleep(500)
  println("2.", stateAgent.get)

  Thread.sleep(100)
  system shutdown
}