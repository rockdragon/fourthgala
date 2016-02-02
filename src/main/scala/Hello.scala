import scalaz._, std.AllInstances._

object Hello {
  def positive(i: Int): Validation[List[String], Int] = {
    if (i > 0) Success(i)
    else Failure(List(s"Nonpositive integer $i"))
  }

  def main(args: Array[String]): Unit = {
    for {
      i1 <- positive(5)
      i2 <- positive(10 * i1)
      i3 <- positive(25 * i2)
      i4 <- positive(2 * i3)
    } yield (i1 + i2 + i3 + i4)

    for {
      i1 <- positive(5)
      i2 <- positive(-1 * i1)
      i3 <- positive(25 * i2)
      i4 <- positive(-2 * i3)
    } yield (i1 + i2 + i3 + i4)

    println(positive(5) +++ positive(10) +++ positive(25))

    println(positive(5) +++ positive(-10) +++ positive(25) +++ positive(-30))
  }
}
