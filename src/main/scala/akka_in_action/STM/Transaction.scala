package akka_in_action.STM

import scala.concurrent.ExecutionContext.Implicits.global
import akka.agent.Agent

import scala.concurrent.Await
import scala.concurrent.stm._
import scala.concurrent.duration._

object Transaction extends App {
  def transfer(from: Agent[Int], to: Agent[Int], amount: Int, toThrow: Boolean): Boolean = {
    // safe & coordinated
    atomic { implicit tnx => {
      if (from.get < amount) false
      else {
        from send (_ - amount)
        if(toThrow) throw new Exception("holy crap")
        to send (_ + amount)
        true
      }
    }}
  }

  def doTransfer(toThrow: Boolean): Unit = {
    val from = Agent(100)
    val to = Agent(20)
    var ok = false
    try {
      ok = transfer(from, to, 50, toThrow)
    } catch {
      case _ =>
    } finally {
      val f = Await.result(from.future, 1 seconds)
      val t = Await.result(to.future, 1 seconds)
      println(ok, f, t)
    }
  }

  doTransfer(false)

  Thread.sleep(1000)

  doTransfer(true)
}
