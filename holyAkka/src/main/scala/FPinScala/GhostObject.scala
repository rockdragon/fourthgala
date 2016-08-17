package FPinScala

import scala.util.Try

object GhostObject extends App{

  object A {
    def show:Unit = a.show
  }

  var a = A
  a = null
  var b = a.show _

  println(Try(a), Try(b()))
}
