package algorithms

/**
  * Created by moye on 16/6/25.
  */
trait Iterator[T] {
  def hasNext: Boolean

  def next: T

  def remove(): Unit
}

class ReverseArrayIterator[T:Manifest](val N: Int) extends Iterator[T] {
  var i: Int = N
  val a = new Array[T](i)

  def hasNext: Boolean = i > 0

  def next: T = {
    i -= 1
    a(i)
  }

  def remove(): Unit = {
  }
}

object holy3 extends App {
  val ri = new ReverseArrayIterator[String](3)
  println(ri.hasNext)
}