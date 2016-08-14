package scala_programming_2nd

import scala.collection.immutable._

object main {

  def indexes(s: String): Map[Char, Set[Int]] = {
    val m = Map[Char, Set[Int]]()
    val pair = s.foldLeft((m, 0))((p, c) =>
      (p._1 + (c -> (p._1.getOrElse(c, Set[Int](p._2)) + p._2)), p._2 +1)
    )
    pair._1
  }

  def main(args: Array[String]): Unit = {
    println(indexes("Mississippi"))
  }

}