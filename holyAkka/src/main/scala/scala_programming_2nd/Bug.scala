package scala_programming_2nd

object then
object show
object around

class Bug(var pos:Int = 0) {
  var forward: Int = 1

  def move(n: Int):this.type = {
    pos += forward * n
    this
  }


  def and(o: show.type): this.type = {
    println(pos)
    this
  }
  def and(o: then.type): this.type = this

  def turn(o: around.type): this.type = {
    forward = -forward
    this
  }
}

object main {
  def main(args: Array[String]): Unit = {
    val bugsy = new Bug()

    bugsy move 4 and show and then move 6 and show turn around move 5 and show
  }
}