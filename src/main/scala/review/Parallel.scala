package review.parallel

import java.util.concurrent.{Callable, Future, TimeUnit, ExecutorService, Executors}

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

    def map[A, B](pa: Par[A])(f: A => B): Par[B] =
      map2(pa, unit(()))((a, _) => f(a))

    def parMap[A, B](as: List[A])(f: A => B): Par[List[B]] = {
      as.foldRight(unit(List[B]()))((a: A, pbs: Par[List[B]]) =>
        map(pbs)(f(a) :: _))
    }

    def sequence[A](ps: List[Par[A]]): Par[List[A]] = {
      ps.foldRight(unit(List[A]()))((pa: Par[A], pas: Par[List[A]]) =>
        map2(pa, pas)((a, list) => a :: list)
      )
    }

    def parFilter[A](l: List[A])(f: A => Boolean): Par[List[A]] = {
      val pars: List[Par[List[A]]] =
        l map asyncF((a: A) => if (f(a)) List(a) else List())
      map(sequence(pars))(_.flatten)
    }

    def parListOp[A, B, C](l: List[A])(f: A => B)(op: List[B] => C): Par[C] = {
      val list: List[Par[B]] =
        l map asyncF((a: A) => f(a))
      map(sequence(list))(op)
    }
  }

  // application
  val executorService = Executors.newFixedThreadPool(10);

  // sorting
  def sortPar(parList: Par[List[Int]]): Par[List[Int]] =
    Par.map(parList)(_.sorted)

  val sortedPar = sortPar(Par.unit(List(10 to 1 by -1: _*)))
  println(run(executorService)(sortedPar).get())

  // count
  val words = List("hello", "world")
  val countedPar = Par.parListOp(words)(_.length)(_.sum)
  println(run(executorService)(countedPar).get())

  // teardown
  executorService.shutdown()

}
