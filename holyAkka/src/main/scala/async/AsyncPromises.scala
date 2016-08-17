package async

import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object AsyncPromises extends App {
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

