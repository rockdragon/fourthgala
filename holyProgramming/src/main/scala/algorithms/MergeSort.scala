
package algorithms

object MergeSort extends App {
  def printArray[T: Manifest](array: Array[T]): Unit = {
    println(array.mkString(","))
  }

  def merge[T: Ordering](left: Array[T], right: Array[T])(implicit ev: Manifest[T]): Array[T] = {
    import Ordered._
    var l = left
    var r = right
    var result = new Array[T](0)
    while (l.length > 0 && r.length > 0) {
      if (l.head < r.head) {
        result = result :+ l.head
        l = l.drop(1)
      } else {
        result = result :+ r.head
        r = r.drop(1)
      }
    }
    result ++ l ++ r
  }

  def mergeSort[T: Ordering](array: Array[T])(implicit ev: Manifest[T]): Array[T] = {
    val mid = array.length / 2
    if (mid < 2) {
      array
    }
    else {
      val parts = array.splitAt(mid)
      merge(mergeSort(parts._1), mergeSort(parts._2))
    }
  }

  printArray(mergeSort(Array(2, 3, 5, 6, 7, 23, 4, 8, 9)))
}
