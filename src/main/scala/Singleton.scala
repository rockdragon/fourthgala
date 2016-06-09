package me.moye.practise.singleton

class Document {
  def setTitle(title: String): this.type = {this}
  def setAuthor(author: String): this.type = {this}
}
class Book extends Document {
  def addChapter(chapter:String) = {this}
}

object main {
  def main(args:Array[String]):Unit = {
    val b = new Book()
    b.setTitle("hello").setAuthor("crapper").addChapter("bravo!")
  }
}