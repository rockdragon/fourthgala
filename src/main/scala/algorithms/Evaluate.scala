package algorithms

import java.util.Stack

import scala.io.StdIn

/**
  * Created by moye on 16/6/22.
  */
class Evaluate extends App{
  val ops = new Stack[String]()
  val vals = new Stack[Double]()
  val str = StdIn.readLine()
  val parts = str.split(" ")
  for (p <- parts) {
    p match {
      case _ if p == "+" || p == "-" || p =="*" || p=="/" => ops.push(p)
      case ")" => {
        val op = ops.pop()
        val v = vals.pop()
        op match {
          case "+" => vals.pop() + v
        }
      }
    }
  }
}
