package async

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.util.Random

object Sleep extends App {
  def sleep(duration: Long) {
    Thread.sleep(duration)
  }

  println("1. Starting...")

  val f = Future {
    sleep(Random.nextInt(500))
    42
  }

  println("2. before onComplete")
  f onComplete {
    case Success(value) => println(s"Got value: $value")
    case Failure(e) => e.printStackTrace
  }

  println("A ..."); sleep(100)
  println("B ..."); sleep(100)
  println("C ..."); sleep(100)
  println("D ..."); sleep(100)
  println("E ..."); sleep(100)
  println("F ..."); sleep(100)
}
