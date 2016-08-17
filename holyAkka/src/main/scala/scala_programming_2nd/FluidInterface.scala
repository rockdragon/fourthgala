package scala_programming_2nd

object Title
object Author

class Book(var title: String = "", var author: String = "") {
  var nextArg:Any = null

  def set(o: Title.type): this.type = {
    nextArg = o
    this
  }
  def set(o:Author.type): this.type = {
    nextArg = o
    this
  }
  def to(s:String): this.type = {
    if(nextArg == Title) title = s else author = s
    this
  }

  override def toString:String = "<" + title + "> by " + author
  def display(): Unit = println(this)
}

object main3 {
  def main(args: Array[String]): Unit = {
    val book = new Book()
    book set Title to "Scala for the impatient" set Author to "Cay Horstman" display
  }
}