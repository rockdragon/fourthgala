package scala_programming_2nd

object main11 {

  def map2[A, B, C](a: Option[A], b:Option[B])(f: (A, B) => C): Option[C] =
    a flatMap(aa =>
      b map (bb =>
        f(aa, bb)))

  def main(args: Array[String]): Unit = {
    val a: Option[Int] = Some(3)
    val b: Option[Int] = Some(4)
    println(map2(a, b)((a, b) => a + b))
  }

}