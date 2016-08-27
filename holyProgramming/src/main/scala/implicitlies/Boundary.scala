package implicitlies

object main extends App {

  class Crap(val value: Int) {
    override def toString = "[Crap: " + value + " ]"
  }

  class Pair[T: Ordering](val first: T, val second: T) {
    def smaller = {
      import Ordered._
      if (first < second) first else second
    }
  }

  implicit object CrapOrdering extends Ordering[Crap] {
    def compare(a: Crap, b: Crap) = a.value - b.value
  }

  val p = new Pair(new Crap(10), new Crap(20))
  println("smaller: ", p.smaller)

  println("phantom is: ", implicitly[Ordering[Crap]])
}
