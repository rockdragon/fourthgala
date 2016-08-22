package scala_programming_2nd

object main2 {
  type Closable = {def close(): Unit}
  def executor(target: Closable, handler: Closable => Unit): Unit = {
    try{
      handler(target)
    } catch {
      case e: Exception =>
    } finally{
      target.close()
    }
  }

  class Affiar(val name:String="") {
    def close(): Unit = println(name + " was closed.")
    override def toString = "(Affiar): " + name
  }

  def Handle(o:Closable):Unit = {
    println(o)
  }

  def main(args: Array[String]):Unit = {
    executor(new Affiar("Jack"), Handle)
  }
}
