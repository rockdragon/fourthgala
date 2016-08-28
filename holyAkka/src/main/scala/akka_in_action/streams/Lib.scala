
package akka_in_action.streams

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object Lib {
  def consume[T](f: Future[T])(implicit executor: ExecutionContext) : Future[T] = {
    f onComplete {
      case Success(t: T) => println(t)
      case Failure(e) => println(e)
    }

    f
  }
}