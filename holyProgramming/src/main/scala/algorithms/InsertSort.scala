package algorithms

object InsertSort extends App {
  def exchange[T](array: Array[T], i: Int, j: Int): Unit = {
    val t = array(i)
    array(i) = array(j)
    array(j) = t
  }

  def sort[T](array: Array[T])(implicit ev:T => Ordered[T]): Unit = {
    for (i <- array.indices)
      for (j <- i to 0 by -1)
        if (j > 0 && array(j) < array(j - 1))
          exchange(array, j, j - 1)
  }

  def printArray[T](array: Array[T]): Unit = {
    print("after holy insertion: ")
    for (i <- array.indices) print(array(i) + ",")
    print("\n")
  }

  val array = Array(9 to 1 by -1: _*)
  sort(array)
  printArray(array)
}
