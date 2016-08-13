package akka_in_action.STM

import scala.concurrent.ExecutionContext.Implicits.global
import akka.agent.Agent
import scala.concurrent.stm._

object Transfer extends App {
  def transfer(from: Agent[Int], to: Agent[Int], amount: Int): Boolean = {
    // safe & coordinated
    atomic { implicit tnx => {
      if (from.get < amount) false
      else {
        from send (_ - amount)
        throw new Exception("holy crap")
        to send (_ + amount)
        true
      }
    }}
  }

  val from = Agent(100)
  val to = Agent(20)
  try {
    transfer(from, to, 50)
  } catch {
    case _ =>
  } finally {
    println(from.future, to.future)
  }
}
