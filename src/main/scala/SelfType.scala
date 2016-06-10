package me.moye.practise.selfType

abstract class Dim[T](val value: Double, val name: String) {
  this: T =>
  protected def create(v: Double): T
  def +(other: Dim[T]) = create(value + other.value)
  override def toString = value + " " + name
}

class Seconds(v: Double) extends Dim[Seconds](v, "s") {
  override def create(v:Double) = new Seconds(v)
}
class Meters(v: Double) extends Dim[Meters](v, "m") {
  override def create(v:Double) = new Meters(v)
}


object main {
  def main(args: Array[String]): Unit = {
    val m = new Meters(100)
    val m2 = new Meters(300)

    println(m + m2)
//    val s = new Seconds(200)
//    println(m + s)
  }
}