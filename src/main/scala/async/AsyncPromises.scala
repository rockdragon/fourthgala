package async

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

object AsyncPromises extends App {
  def produceSomething: String = "Holy high."

  def continueDoingSomethingUnrelated(): Unit = println("do other things.")

  def startDoingSomething(): Unit = println("Starting...")

  def doSomethingWithResult(r: String): Unit = println("Result is:", r)

  val p = Promise[String]()
  val f = p.future

  val producer = Future {
    val r = produceSomething
    p success r
    continueDoingSomethingUnrelated()
  }

  val consumer = Future {
    startDoingSomething()
    f onSuccess {
      case r => doSomethingWithResult(r)
    }
  }


}

