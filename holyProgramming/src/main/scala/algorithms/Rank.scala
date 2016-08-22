package algorithms

object dichotomy extends App {
  var count:Int = 0

  def binary(key: Int, arr: Seq[Int]): Int = {
    return binary(key, 0, arr.length - 1, arr)
  }

  def binary(key:Int, low: Int, high: Int, arr: Seq[Int]): Int = {
    if (low > high) return -1

    val mid = low + (high - low) / 2
    if(mid > arr.length - 1) return -1
    count += 1
    if (key < arr(mid)) binary(key, low, mid - 1, arr)
    else if(key > arr(mid)) binary(key, mid + 1, high, arr)
    else mid
  }


  binary(111, Array(1 to 100: _*))
  println("times: ", count)
}
