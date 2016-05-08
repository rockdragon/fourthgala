package me.moye.practise.structureinfp

object main {

  sealed trait List[+A]

  case object Nil extends List[Nothing]

  case class Cons[+A](head: A, tail: List[A]) extends List[A]

  object List {
    def sum(ints: List[Int]): Int = ints match {
      case Nil => 0
      case Cons(x, xs) => x + sum(xs)
    }

    def product(ds: List[Double]): Double = ds match {
      case Nil => 1.0
      case Cons(0.0, _) => 0.0
      case Cons(x, xs) => x * product(xs)
    }

    def append[A](a1: List[A], a2: List[A]): List[A] =
      a1 match {
        case Nil => a2
        case Cons(h, t) => Cons(h, append(t, a2))
      }

    def foreach[A](as: List[A])(f:(A) => Unit): Unit = {
      as match {
        case Cons(h, t) => {
          f(h)
          foreach(t)(f)
        }
        case _ =>
      }
    }


    def apply[A](as: A*): List[A] =
      if (as.isEmpty) Nil
      else Cons(as.head, apply(as.tail: _*))
  }

  @annotation.tailrec
  def foldLeft[A, B](as: List[A], z: B)(f:(A, B) => B): B = {
    as match {
      case Cons(h, t) => foldLeft(t, f(h, z))(f)
      case Nil => z
    }
  }

  def map[A, B](as: List[A], processed: List[B])(f:(A) => B): List[B] = {
    as match {
      case Nil => processed
      case Cons(h, t) => map(t, List.append(processed, List(f(h))))(f)
    }
  }

  def main(args: Array[String]): Unit = {
    val list = List(1 to 5: _*)
    val x = list match {
      case Cons(x, Cons(2, Cons(4, _))) => x
      case Nil => 42
      case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
      case Cons(h, t) => h + List.sum(t)
      case _ => 101
    }

    println(x)

    println(foldLeft(List(1 to 15: _*), 1)((a, b) => a * b))

    val chars = map(List('a' to 'f': _*), Nil)(c => (c + 1).toChar)
    List.foreach(chars)(ch => println(ch))
  }

}