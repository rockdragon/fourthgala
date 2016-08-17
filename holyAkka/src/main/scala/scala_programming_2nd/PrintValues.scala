package scala_programming_2nd

object main13 {
  def printValues(f: Int => Int, from: Int, to: Int): Seq[Int] = {
    for (i <- from to to) yield f.apply(i)
  }

  def main(args: Array[String]): Unit = {
    println(printValues((x: Int) => x * x, 3, 6))

    println(printValues(Array(1, 1, 2, 3, 5, 8, 13, 21, 34, 55), 3, 6))
  }
}