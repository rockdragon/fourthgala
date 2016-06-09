package me.moye.practise.pairs

class Pair[T, S](val p: (T, S)) {
  def swap = (p._2, p._1)
}

class VairablePair[T](var p: (T, T)) {
  def swap = {
    (p._2, p._1)
  }
}

class InheritedPair[T](val first: T, val second: T) {
  def replaceFirst(newFirst: T) = new InheritedPair[T](newFirst, second)
  override def toString = "(" + first + "," + second + ")"
}
class Person
class Student extends Person

class HolyPair[+T](val first: T, val second: T) {
  def replaceFirst[R >: T](newFirst: R) = new HolyPair[R](newFirst, second)
  override def toString = "(" + first + "," + second + ")"
}
class NastyDoublePair[Double](override val first: Double, override val second: Double)
  extends HolyPair[Double](first, second) {
//  override def replaceFirst(newFirst: Double) = new NastyDoublePair(math.sqrt(newFirst), second)
}

class ExchangeablePair[T, S](private var first: T, private var second: S) {
  def swap(implicit ev1: T =:= S, ev2: S =:= T) = {
    val temp = first
    first = second
    second = temp
  }
}

object main {
  def middle[T](t: Iterable[T]): T = {
    val list = t.toList
    list(list.size / 2)
  }

  def main(args: Array[String]): Unit = {
    val p = new Pair(3, 4)
    val s = p.swap
    println("swapped:", s)

    var p2 = new VairablePair(1, 2)
    val s2 = p2.swap
    println("swapped:", s2)

    val p1 = new Person
    val p3 = new Person
    val s1 = new Student
    val pair = new InheritedPair(p1, p3)
    pair.replaceFirst(s1)
    println(pair)

    println(middle("hello"))

    val n = new NastyDoublePair(1200, 1500)
    val sss = n.replaceFirst(2100)
    println(sss)
  }
}