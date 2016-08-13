package akka_in_action.STM

import scala.concurrent.ExecutionContext
import scala.concurrent.stm.{Ref, atomic, retry}

object STMLoot extends App {
  implicit val ec = ExecutionContext.global

  val availableSeats = Ref(Seq[Seat]())

  // trigger
  //  atomic { implicit txn =>
  //    availableSeats.set(Seq(Seat(2)))
  //  }

  val mySeat = atomic { implicit txn => {
    val allSeats = availableSeats()
    if (allSeats.isEmpty)
      retry
    val reservedSeat = allSeats.head
    availableSeats() = availableSeats().tail
    Some(reservedSeat)
  }
  } orElse {
    None
  }

  println(mySeat)
}
