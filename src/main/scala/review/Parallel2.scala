package review.parallel2

import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.{ ExecutorService, CountDownLatch, Callable, Executors }
import akka.actor.{ Actor, Props, ActorSystem }

class MyActor extends Actor {
  def receive = {
    case msg => println(s"Got message: ${msg}")
  }
}

// example:
//  val system = ActorSystem("mySystem")
//  val actor = system.actorOf(Props[MyActor])
//  actor ! "Hello"


object Parallel2 extends App {
  type Par[A] = ExecutorService => Future[A]
  type SideEffect = Unit

  sealed trait Future[A] {
    private[Parallel2] def apply(k: A => SideEffect): SideEffect
  }

  def run[A](es: ExecutorService)(p: Par[A]): A = {
    val ref = new AtomicReference[A]

    val latch = new CountDownLatch(1)

    p(es) { a => ref.set(a); latch.countDown }

    latch.await
    ref.get
  }

  def unit[A](a: A): Par[A] = es => new Future[A] {
    def apply(cb: A => SideEffect): SideEffect = cb(a)
  }

  def eval(es: ExecutorService)(r: => SideEffect): SideEffect =
    es.submit(new Callable[SideEffect] { def call = r })

  def fork[A](a: => Par[A]): Par[A] = es => new Future[A] {
    def apply(cb: A => SideEffect): SideEffect =
      eval(es)(a(es)(cb))
  }

//  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =


}
