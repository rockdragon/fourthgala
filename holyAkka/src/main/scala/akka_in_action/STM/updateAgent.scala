package akka_in_action.STM

import akka.agent.Agent

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.stm.{Ref, atomic}
import scala.concurrent.duration._

object updateAgent extends App {
  implicit val ec =  ExecutionContext.global

  val numberUpdates = Agent(0)
  val count = Ref(5)

  Future {
    for (i <- 0 until 10) {
      println("count() inc ---->>>")
      atomic { implicit txn =>
        count() = count() + 1
      }
      Thread.sleep(50)
    }
  }
  var nrRuns = 0
  val myNumber = atomic { implicit txn => {
    nrRuns += 1
    numberUpdates send (_ + 1)
    count()
    Thread.sleep(100)
    count()
  }}

  println("nrRuns", nrRuns)
  println("myNumber", myNumber)
  Await.ready(numberUpdates.future(), 1 seconds)
  println("numberUpdates", numberUpdates.get)
}
