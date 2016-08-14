package scala_programming_2nd

object main19 {

  sealed trait Tree[+A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  object Tree {
    def size[A](a: Tree[A], i: Int): Int = {
      a match {
        case Leaf(_) => i + 1
        case Branch(l, r) => size(l, i) + size(r, 1)
      }
    }

    def apply[A](as: A): Tree[A] = Leaf(as)
    def apply[A](l:Tree[A], r:Tree[A]): Tree[A] = Branch(l, r)
  }

  def main(args: Array[String]): Unit = {
    val l = Leaf(10)
    val r = Leaf(20)
    val b = Branch(l, r)
    println(Tree.size(b, 0))
  }

}