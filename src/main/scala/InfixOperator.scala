package me.moye.practise.infixOperator

object main {
  def approximate(array:Seq[Int], value:Int): Int Either Int = {
    if(array.contains(value)) {
      Left(array.indexOf(value))
    } else {
      Right(array.indexOf(array.reduce((a, b)=> if(math.abs(value-a) > math.abs(value-b)) b else a)))
    }
  }

  def main(args: Array[String]):Unit = {
    val array = Array(1 to 20 by 3: _*)
    val value = 12

    println(approximate(array, value))
  }
}