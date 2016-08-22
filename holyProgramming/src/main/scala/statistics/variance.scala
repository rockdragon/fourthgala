
package statistics

import scala.math._

object variance extends App {
  val seqs = List[(Int, Int)](145 -> 4, 155 -> 9, 165 -> 16, 175 -> 27,
    185 -> 20, 195 -> 17, 205 -> 10, 215 -> 8, 225 -> 4, 235 -> 5)

  val x_ = 185
  val n_1 = 120

  val variance = seqs.foldRight(0.0)((p, acc) =>
    pow((p._1 - x_), 2) * p._2 + acc
  )
  println(variance / n_1)
}