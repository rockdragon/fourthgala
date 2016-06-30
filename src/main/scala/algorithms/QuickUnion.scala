package algorithms

object QuickUnion extends App {
  val id = Array(1, 1, 1, 8, 3, 0, 5, 1, 8, 8)

  def find(q: Int): Int = {
    var p = q
    while (p != id(p)) p = id(p)
    p
  }

  println("find(5) :" + find(5))
  println("find(9) :" + find(9))
}
