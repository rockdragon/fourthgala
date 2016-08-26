
package scala_programming_2nd


object StringDestruction extends App {
  def greeting(ag:String, args: String*): Unit = {
    println(ag, args)
  }

  greeting("tom", "sheringham", "was", "a", "bastard")
}
