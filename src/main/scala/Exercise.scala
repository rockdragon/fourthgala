package me.moye.practise

import scala.collection.mutable.ArrayBuffer

object main {
  def exchange(array: Array[Int]) = {
    val length = array.length
    val buffer = ArrayBuffer[Int]()
    buffer ++= array

    for (i <- 0 to length / 2)
      if (i * 2 + 1 < buffer.length) {
        val temp = buffer(i * 2)
        buffer(i * 2) = buffer(i * 2 + 1)
        buffer(i * 2 + 1) = temp
      }


    

    buffer.toArray
  }

  def main(args: Array[String]): Unit = {
    exchange(Array(1, 2, 3, 4, 5))
  }
}