package scala_programming_2nd

object main15 {
  class Document {
    def setTitle(title: String): this.type = {this}
    def setAuthor(author: String): this.type = {this}
  }
  class Book extends Document {
    def addChapter(chapter:String) = {this}
  }

  def main(args:Array[String]):Unit = {
    val b = new Book()
    b.setTitle("hello").setAuthor("crapper").addChapter("bravo!")
  }
}