
package statistics

import scala.math._

object Lib {
  def average(seq: Seq[Double]): Double = seq.sum / seq.length

  def degreeOfFreedom(seq:Seq[Double]): Int = seq.length - 1

  def variance(seq: Seq[Double]): Double = {
    val x_ = average(seq)
    seq.foldRight(0.0)((c, acc) =>
      pow(c - x_, 2) + acc) / degreeOfFreedom(seq)
  }

  def standardVariation(seq: Seq[Double]): Double = {
    val x_ = average(seq)
    val vars = variance(seq)
    sqrt(vars)
  }

  def coefficientVariation(seq: Seq[Double]): Double = {
    val x_ = average(seq)
    val sigma = standardVariation(seq)
    sigma / x_
  }
}
