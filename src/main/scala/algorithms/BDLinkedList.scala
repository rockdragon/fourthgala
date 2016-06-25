package algorithms

/**
  * Created by moye on 16/6/25.
  */
class Node[T] (val value: T, var prev: Node[T] = null, var next: Node[T] = null) {
}

class BDLinkedList[T: Manifest] {
  var first: Node[T] = null
  var last: Node[T] = null
  var current: Node[T] = null
  var tail: Int = 0

  def add(node: Node[T]): Unit = {
    if (first == null) first = node else first.next = node
    last = node
    tail += 1
  }

  def index(n: Int, cur: Int = 1): Node[T] = {
    if (cur == 1) current = first
    else current = current.next

    if (n == cur) current
    else index(n, cur + 1)
  }

  def remove(index: Int): Unit = {
    if (index > tail) throw new ArrayIndexOutOfBoundsException
  }

}

object holy6 extends App {
  val list = new BDLinkedList[String]()
  list.add(new Node("Hello", null, null))
  list.add(new Node("World", null, null))

  val node: Node[String] = list.index(2)
  println(node.value)
}
