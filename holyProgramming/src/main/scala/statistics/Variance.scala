
package statistics

import scala.math._

object Variance extends App {
  def groupedVariance(): Unit = {
    val seqs = List[(Int, Int)](145 -> 4, 155 -> 9, 165 -> 16, 175 -> 27,
      185 -> 20, 195 -> 17, 205 -> 10, 215 -> 8, 225 -> 4, 235 -> 5)

    val x_ = 185
    val n_1 = 120

    val variance = seqs.foldRight(0.0)((p, acc) =>
      pow((p._1 - x_), 2) * p._2 + acc
    ) / n_1
    println(variance)
    println(sqrt(variance))
  }

  def ungroupedVariance(): Unit = {
    val seq = Seq[Double](145, 155, 165, 175, 185, 195, 205, 215, 225, 235)

    val variance = Lib.variance(seq)
    println(variance)
    println(sqrt(variance))
  }

  groupedVariance()
  ungroupedVariance()
}