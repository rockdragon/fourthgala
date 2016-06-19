package implicitlies

object main {
  class Pair[T: Ordering](val first: T, val second: T) {
    def smaller(implicit order: Ordering[T]) =
      if(order.compare(first, second) < 0) first else second
  }

  def main(args: Array[String]): Unit = {
    val p =new  Pair(10, 20)
    println("smaller: ", p.smaller)

    println("phantom is: ", implicitly[Ordering[Int]])
  }
}
