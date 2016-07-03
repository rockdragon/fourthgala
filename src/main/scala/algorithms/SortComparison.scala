package algorithms

import scala.util.Random

object SortComparison extends App {
  def exchange[T](array: Array[T], i: Int, j: Int): Unit = {
    val t = array(i)
    array(i) = array(j)
    array(j) = t
  }

  def insertSort[T](array: Array[T])(implicit ev: T => Ordered[T]): Unit = {
    for (i <- array.indices)
      for (j <- i to 0 by -1)
        if (j > 0 && array(j) < array(j - 1))
          exchange(array, j, j - 1)
  }

  def selectSort[T](array: Array[T])(implicit ev: T => Ordered[T]): Unit = {
    for (i <- array.indices)
      for (j <- i + 1 until array.length)
        if (array(j) < array(i))
          exchange(array, j, i)
  }

  def time(algorithm: String, array: Array[Double]): Double = {
    val t0 = System.nanoTime //unit: ns
    algorithm match {
      case "select" => selectSort(array)
      case "insert" => insertSort(array)
    }
    (System.nanoTime - t0) / 1000000
  }

  def timeRandomInput(algorithm: String, length: Int, total: Int): Double = {
    var totalTime = 0.0
    val array: Array[Double] = new Array(length)
    for (t <- 0 to total) {
      for (i <- 0 until length)
        array(i) = Random.nextDouble
      totalTime += time(algorithm, array)
    }
    totalTime
  }

  val alg1 = "select"
  val alg2 = "insert"
  val t1 = timeRandomInput(alg1, 1000, 10)
  val t2 = timeRandomInput(alg2, 1000, 10)

  println(s"$alg1 is $t1, $alg2 is $t2, ratio is $t2/$t1.")
}
