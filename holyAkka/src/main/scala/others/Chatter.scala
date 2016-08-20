package others

object Chatter {
  def instance = new Chatter

  def print(chatter:Chatter): Unit = println(s"${chatter.name}")
}

class Chatter(private var name: String = "anonymous") {
}

object app extends App {
  Chatter.print(Chatter.instance)
}