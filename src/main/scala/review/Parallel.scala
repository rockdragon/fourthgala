package review

import java.util.concurrent.{Callable, Future, TimeUnit, ExecutorService}

object Parallel extends App {

  type Par[A] = ExecutorService => Future[A]

  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

  object Par {
    def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

    private case class UnitFuture[A](get: A) extends Future[A] {
      def isDone = true

      def get(timeout: Long, units: TimeUnit) = get

      def isCancelled = false

      def cancel(evenIfRunning: Boolean): Boolean = false
    }

    def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
      (es: ExecutorService) => {
        val af = a(es)
        val bf = b(es)
        UnitFuture(f(af.get, bf.get))
      }

    def fork[A](a: => Par[A]): Par[A] =
      es => es.submit(new Callable[A] {
        def call = a(es).get
      })

    def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

    // extensions
    def asyncF[A, B](f: A => B): A => Par[B] =
      a => lazyUnit(f(a))

    def map[A, B](a: Par[A])(f: A => B): Par[B] =
      map2(a, unit(()))((a, _) => f(a))

    def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = {
      ps.foldLeft(unit(List[B]()))((pbs: Par[List[B]], a: A) =>
        map(pbs)(listB => listB :+ f(a)))
    }
  }

  // applications
  def sortPar(parList: Par[List[Int]]): Par[List[Int]] =
    Par.map(parList)(_.sorted)
}
