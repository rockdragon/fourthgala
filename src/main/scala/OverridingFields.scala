package me.moye.overridingFields

trait AbstratT2 {
  println("In AbstractT2")
  val value: Int
  lazy val inverse = 1.0 / value
}

object main {
  def main(args: Array[String]):Unit = {
    val obj = new AbstratT2 {
      println("In Obj")
      val value: Int = 10
    }

    println("obj.value = " + obj.value + ", inverse = " + obj.inverse)
  }
}