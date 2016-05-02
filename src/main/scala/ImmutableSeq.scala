import scala.collection.immutable._

object main {

  def indexes(s: String): Map[Char, Set[Int]] = {
    val m = Map[Char, Set[Int]]()
    val pair = s.foldLeft((m, 0))((p, c) => {
      if (!p._1.isDefinedAt(c)) {
        (p._1 + (c -> Set[Int](p._2)), p._2 + 1)
      } else {
        (p._1 + (c -> (p._1(c) + p._2)), p._2)
      }
    })
    pair._1
  }

  def main(args: Array[String]): Unit = {
    println(indexes("Mississippi"))
  }

}