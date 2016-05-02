package me.moye.practise.seqAggregate

import scala.collection.immutable._

object main {

  def aggr(s: String): Map[Char, Int] = {
    s.par.aggregate(Map[Char, Int]())(
      (coll, ch) => coll + (ch -> (coll.getOrElse(ch, 0) + 1)),
      (prev, curr) => {
        curr.foldLeft(prev)((p, c) => p + (c._1 -> (p.getOrElse(c._1, 0) + c._2)))
      }
    )
  }

  def main(args: Array[String]): Unit = {
    println(aggr("Mississippi"))
  }

}