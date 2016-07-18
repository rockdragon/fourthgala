package review.parallel2

import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent._

object Parallel2 extends App {

  sealed trait Future[A] {
    private[Parallel2] def apply(k: A => Unit): Unit
  }

  type Par[A] = ExecutorService => Future[A]

  def run[A](es: ExecutorService)(p: Par[A]): A = {
    val ref = new AtomicReference[A]

    val latch = new CountDownLatch(1)

    p(es) { a => ref.set(a); latch.countDown }

    latch.await
    ref.get
  }

}
