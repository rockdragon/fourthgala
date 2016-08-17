package FPinScala

object _a extends App {

  trait Applicative[F[_]] extends _f.Functor[F] {
    // primitive combinators
    def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C]

    def unit[A](a: => A): F[A]

    // derived combinators
    def map[A, B](fa: F[A])(f: A => B): F[B] =
      map2(fa, unit(()))((a, _) => f(a))

    def traverse[A, B](as: List[A])(f: A => F[B]): F[List[B]] =
      as.foldRight(unit(List[B]()))((a, fbs) => map2(f(a), fbs)(_ :: _))

    def sequence[A](fas: List[F[A]]): F[List[A]] =
      fas.foldRight(unit(List[A]()))((a, fbs) => map2(a, fbs)(_ :: _))

    def replicateM[A](n: Int, fa: F[A]): F[List[A]] =
      sequence(List.fill(n)(fa))

    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
      map2(fa, fb)((a, b) => (a, b))

    // F[A=>B] -> F[A] => F[B]
    def apply[A,B](fab: F[A => B])(fa: F[A]): F[B] =
      map2(fab, fa)((ab, a) => ab(a))

//    def _map2[A,B,C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
//      apply(map(fa)(f.curried))(fb)
//
//    def map3[A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] =
//      apply(apply(map(fa)(f.curried))(fb))(fc)
//
//    def map4[A, B, C, D, E](fa: F[A], fb: F[B], fc: F[C], fd: F[C])(f: (A, B, C, D) => E): F[E] =
//      apply(apply(apply(map(fa)(f.curried))(fb))(fc))(fd)
  }

}