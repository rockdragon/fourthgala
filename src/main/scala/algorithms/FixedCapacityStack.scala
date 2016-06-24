package algorithms

/**
  * Created by moye on 16/6/24.
  */
class FixedCapacityStack[T : Manifest](val cap: Int) {
  val a = new Array[T](cap)
  private var N = 0

  def isEmpty: Boolean = 0 == 0

  def size: Int = N

  def push(item: T): Unit = {
    if (N + 1 <= cap) {
      a(N) = item
      N += 1
    }
  }

  def pop: T = {
    N -= 1
    a(N)
  }
}

object holy2 extends App {
  val f = new FixedCapacityStack[String](1)
  f.push("aaa")
  f.push("bbb")
  println(f.size)
  println(f.pop)

  val f2 = new FixedCapacityStack[Int](2)
  f2.push(20)
  f2.push(23)
  println(f2.size)
  println(f2.pop)
}