package scala_programming_2nd

import scala.collection.mutable.ArrayBuffer

class Network {
  class Member(val name: String) {
    val contacts = members

    def equals(m:Network#Member):Boolean = m.contacts == this.contacts
  }

  private val members = new ArrayBuffer[Network#Member]

  def join(name:String) = {
    val m = new Member(name)
    members += m
    m
  }
}


object main {
  def main(args: Array[String]): Unit = {
    val qq = new Network
    val wx = new Network

    val jack = qq.join("jack")
    val tom = qq.join("tom")

    println(jack.equals(tom))

    val andy = wx.join("andy")

    println(jack.equals(andy))
  }
}