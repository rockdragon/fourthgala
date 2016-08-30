
package Variance

object AdvanceType extends App {
  trait Container[M[_]] { def put[A](x: A): M[A]; def get[A](m: M[A]): A }

  val container = new Container[List] { def put[A](x:A) = List(x); def get[A](m:List[A]) = m.head }
  val c = container.put("123")
  println(container.get(c))

  trait HolyContainer[A <: HolyContainer[A]] extends Ordered[A]
  class MyContainer extends HolyContainer[MyContainer] {
    def compare(that: MyContainer): Int = ???
  }

}
