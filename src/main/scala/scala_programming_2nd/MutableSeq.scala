package scala_programming_2nd

import scala.collection.mutable._

object main9 {

  def indexes(s: String): LinkedHashMap[Char, SortedSet[Int]] = {
    var m = LinkedHashMap[Char, SortedSet[Int]]()
    s.foldLeft(0)((p, c) => {
      if (!m.isDefinedAt(c)) m += (c -> SortedSet[Int]())
      m(c) += p
      p +1
    })
    m
  }

  def main(args: Array[String]): Unit = {
    println(indexes("Mississippi"))
  }

}