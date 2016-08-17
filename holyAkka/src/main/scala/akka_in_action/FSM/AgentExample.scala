package akka_in_action.FSM

import akka.actor.ActorSystem
import akka.agent.Agent
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

case class BookStatics(val nameBook: String, nrSold: Int)

case class StateBookStatics(val sequence: Long,
                            books: Map[String, BookStatics])

object AgentSample extends App {
  implicit val system = ActorSystem("actor-system")

  import system.dispatcher

  implicit val timeout = Timeout(1000 millis)

  val stateAgent = Agent(StateBookStatics(0, Map()))
  for(state <- stateAgent) {
    println("1.", state)
  }

  val newState = StateBookStatics(3333, Map("book" -> BookStatics("book", 3333)))
  stateAgent send newState

  val future = stateAgent alter(state =>
    state.copy(state.sequence,state.books)
  )

  val state2 = Await.result(future, 1 seconds)
  println("2.", state2)

  system shutdown
}