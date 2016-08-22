package algorithms

import scala.util.Random

object SortComparison extends App {
  def printArray[T](array: Array[T]): Unit = {
    print("after holy insertion: ")
    for (i <- array.indices) print(array(i) + ",")
    print("\n")
  }

  def exchange[T](array: Array[T], i: Int, j: Int): Unit = {
    val t = array(i)
    array(i) = array(j)
    array(j) = t
  }

  def insertSort[T](array: Array[T])(implicit ev: T => Ordered[T]): Unit = {
    for (i <- array.indices)
      for (j <- i to 0 by -1 if j > 0 && array(j) < array(j - 1))
        exchange(array, j, j - 1)
  }

  def selectSort[T](array: Array[T])(implicit ev: T => Ordered[T]): Unit = {
    for (i <- array.indices)
      for (j <- i + 1 until array.length if array(j) < array(i))
        exchange(array, j, i)
  }

  def hillSort[T](array: Array[T])(implicit ev: T => Ordered[T]): Unit = {
    val length = array.length
    var h = 1
    while (h < length / 3) h = 3 * h + 1
    while (h >= 1) {
      for (i <- h until length)
        for (j <- i to h by -h if j >= h && array(j) < array(j - h))
          exchange(array, j, j - h)
      h = h / 3
    }
  }

  def time(algorithm: String, array: Array[Double]): Double = {
    val t0 = System.nanoTime //unit: ns
    algorithm match {
      case "select" => selectSort(array)
      case "insert" => insertSort(array)
      case "hill" => hillSort(array)
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
  val alg3 = "hill"
  val t1 = timeRandomInput(alg1, 1000, 10)
  val t2 = timeRandomInput(alg2, 1000, 10)
  val t3 = timeRandomInput(alg3, 1000, 10)

  println(s"$alg1 is $t1, $alg2 is $t2, $alg3 is $t3")
}
