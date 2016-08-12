package akka_in_action.STM

object Synchronized extends App {
  var availableSeats = Array(1 to 10: _*)

  val reservedSeat = availableSeats.synchronized {
    val head = availableSeats.head
    availableSeats = availableSeats.tail
    head
  }

  println(reservedSeat, "[", availableSeats.mkString(","), "]")
}
