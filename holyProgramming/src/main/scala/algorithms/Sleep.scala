package algorithms

import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

object Sleep extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

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
