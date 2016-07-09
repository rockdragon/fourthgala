package FPinScala

object _m extends App {

  trait Monad[F[_]] extends _f.Functor[F] {

    def unit[A](a: => A): F[A]

    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

    def map[A, B](ma: F[A])(f: A => B): F[B] =
      flatMap(ma)(a => unit(f(a)))

    def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
      flatMap(fa)(a => map(fb)(b => f(a, b)))

    def sequence[A](lma: List[F[A]]): F[List[A]] =
      lma.foldRight(unit(List[A]()))((ma, mla) => map2(ma, mla)(_ :: _))

    def traverse[A, B](la: List[A])(f: A => F[B]): F[List[B]] =
      la.foldRight(unit(List[B]()))((a, mlb) => map2(f(a), mlb)(_ :: _))

    def _replicateM[A](n: Int, ma: F[A]): F[List[A]] =
      if (n <= 0) unit(List[A]()) else map2(ma, _replicateM(n-1, ma))(_ :: _)

    def replicateM[A](n: Int, ma: F[A]): F[List[A]] =
      sequence(List.fill(n)(ma))

    def product[A, B](ma: F[A], mb: F[B]): F[(A, B)] =
      map2(ma, mb)((_, _))

    def compose[A,B,C](f: A => F[B], g: B => F[C]): A => F[C] =
      a => flatMap(f(a))(g)

    def filterM[A](ms: List[A])(f: A => F[Boolean]): F[List[A]] =
      ms.foldRight(unit(List[A]()))((x, y) =>
        compose(f, (b: Boolean) => if (b) map2(unit(x), y)(_ :: _) else y)(x))
  }

}