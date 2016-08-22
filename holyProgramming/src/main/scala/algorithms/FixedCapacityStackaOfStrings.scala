package algorithms

/**
  * Created by moye on 16/6/24.
  */
class FixedCapacityStackaOfStrings(val cap: Int) {
  val a = new Array[String](cap)
  private var N = 0

  def isEmpty: Boolean = 0 == 0

  def size: Int = N

  def push(item: String): Unit = {
    if (N + 1 <= cap) {
      a(N) = item
      N += 1
    }
  }

  def pop: String = {
    N -= 1
    a(N)
  }
}

object holy extends App {
  val f = new FixedCapacityStackaOfStrings(1)
  f.push("aaa")
  f.push("bbb")
  println(f.size)
}