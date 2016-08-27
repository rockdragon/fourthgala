package algorithms

import scala.concurrent.{Future, Promise}

object AsyncPromises extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  def sleep(duration: Long) {
    Thread.sleep(duration)
  }

  def produceSomething: String = "Holy high."

  def continueDoingSomethingUnrelated(): Unit = println("do other things.")

  def startDoingSomething(): Unit = println("Starting...")

  def doSomethingWithResult(r: String): Unit = println("Result is:", r)

  val p = Promise[String]()
  val f = p.future

  val producer = Future {
    val r = produceSomething
    sleep(200)
    p success r
    continueDoingSomethingUnrelated()
  }

  val consumer = Future {
    startDoingSomething()
    f onSuccess {
      case r => doSomethingWithResult(r)
    }
  }

  consumer onSuccess {
    case _ => println("DONE")
  }

}
