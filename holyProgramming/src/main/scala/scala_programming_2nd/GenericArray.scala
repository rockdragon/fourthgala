package scala_programming_2nd

class GenericArray [T: Manifest](val first: T, val second: T) {
  val r = new Array[T](2)
  r(0) = first
  r(1) = second
}

object main5 {
  def main(args: Array[String]):Unit = {
    val g = new GenericArray[Int] (10, 20)
    println(g.r(0))
    println(g.r(1))
  }
}