package akka_in_action.STM

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.concurrent.stm.{Ref, atomic}

case class Seat(index: Int)

object STM extends App {
  implicit val ec = ExecutionContext.global

  val seats =  (for (i <- 1 to 10) yield Seat(i))
  var availableSeats = Ref(seats)

  val f = Future {
    for (i <- 1 to 10) {
      val seat = atomic { implicit txn => {
        val head = availableSeats().head
        availableSeats() = availableSeats().tail
        head
      }}
      println(seat)
      Thread.sleep(50)
    }
  }

  Await.ready(f, 2 second)
}
