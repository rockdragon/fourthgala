package me.moye.practise

import scala.collection.mutable._

object main {

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