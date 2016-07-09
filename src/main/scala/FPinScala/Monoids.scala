package FPinScala

object _Monoids extends App {

  trait Foldable[F[_]] {
    def foldRight[A, B](as: F[A])(z: B)(f: (A, B) => B): B

    def foldLeft[A, B](as: F[A])(z: B)(f: (B, A) => B): B

    def foldMap[A, B](as: F[A])(f: A => B)(mb: Monoid[B]): B

    def concatenate[A](as: F[A])(m: Monoid[A]): A =
      foldLeft(as)(m.zero)(m.op)
  }

  def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B =
    as.foldLeft(m.zero)((b, a) => m.op(b, f(a)))

  def foldMapV[A, B](as: IndexedSeq[A], m: Monoid[B])(f: A => B): B = {
    if (as.length == 0)
      m.zero
    else if (as.length == 1)

      f(as(0))
    else {
      val (l, r) = as.splitAt(as.length / 2)
      m.op(foldMapV(l, m)(f), foldMapV(r, m)(f))
    }
  }

  def mapMergeMonoid[K, V](V: Monoid[V]): Monoid[Map[K, V]] =
    new Monoid[Map[K, V]] {
      def zero = Map[K, V]()

      def op(x: Map[K, V], y: Map[K, V]) =
        (x.keySet ++ y.keySet).foldLeft(zero) { (acc, k) =>
          acc.updated(k, V.op(x.getOrElse(k, V.zero),
            y.getOrElse(k, V.zero))
          )
        }
    }

  val intAddition: Monoid[Int] = new Monoid[Int] {
    def op(x: Int, y: Int) = x + y

    val zero = 0
  }
  val M: Monoid[Map[String, Map[String, Int]]] = mapMergeMonoid(mapMergeMonoid(intAddition))

  val m1 = Map("o1" -> Map("i1" -> 1, "i2" -> 2))
  val m2 = Map("o1" -> Map("i2" -> 3))
  val m3 = M.op(m1, m2)

  println("merged:", m3)

  trait Monoid[A] {
    def op(a1: A, a2: A): A
    def zero: A
  }

  def functionMonoid[A, B](B: Monoid[B]): Monoid[A => B] =
    new Monoid[A => B] {
      def zero = a => B.zero

      def op(x: A => B, y: A => B) = a => B.op(x(a), y(a))
    }

  val stringMonoid = new Monoid[String] {
    def op(a1: String, a2: String) = a1 + a2
    val zero = ""
  }

  val F: Monoid[Int => String] = functionMonoid(stringMonoid)
  val f = F.op(x => x.toString, y => y.toString)
  println("F:", f(10)) // output :=> 1010

  def bag[A](as : IndexedSeq[A]): Map[A, Int] =
    as.foldLeft(Map[A, Int]()) { (acc, k) =>
      acc.updated(k, acc.getOrElse(k, 0) + 1)
    }

  val b = bag(Vector("a", "rose", "is", "a", "rose"))
  println("aggreated bag:", b)
}
