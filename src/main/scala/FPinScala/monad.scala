package FPinScala

object monad extends App {

  trait Monad[M[_]] {
    def id[X](x: X): M[X]

    def flatMap[X, Y](xs: M[X], f: X => M[Y]): M[Y]
  }

}