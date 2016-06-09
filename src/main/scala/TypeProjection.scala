package me.moye.practise.typeProjection

object Holy{

  class High(val name: String) {
    override def toString = "(name:" + name + ")"
  }

  type Crap = High
  def crapper(name:String):Crap = {
    val crapper = new Crap(name)
    crapper
  }
}

object main{
  def main(args:Array[String]):Unit = {
    val crapper = Holy.crapper("holy crap!")
    println(crapper)
  }
}