package cps

import scala.util.continuations._

object app extends App {
  var cont: (Unit => String) = null

  def visit(a: List[String]): String @cps[String] = {
    if(a.isEmpty) "" else {
      shift {
        k: (Unit => String) => {
          cont = k
          a.head
        }
      }
      println(a.head)
      visit(a.tail)
    }
  }

  reset {
    visit(List("Hi", "Guy"))
  }
  cont()
}