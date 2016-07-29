package algorithms

class RingBuffer[T: Manifest](capacity: Int) {
  private var _head = 0
  private var _tail = 0
  private var _size = 0
  private val queue = new Array[T](capacity)

  def size: Int = this._size

  def isEmpty: Boolean = this._size == 0

  def isFull: Boolean = this._size == capacity

  def peek: T = this.queue(this._head)

  def enqueue(elem: T): Int = {
    this.queue(this._tail) = elem

    if (!this.isFull) this._size += 1 else this._head += 1
    this._tail = (this._head + this._size) % this.capacity

    //println("enqueue", this.queue.mkString(","))

    this.size
  }

  def dequeue: T = {
    if (this.isEmpty) throw new Exception("RingBuffer is empty")

    val elem = this.peek

    this._size -= 1
    this._head = (this._head + 1) % this.capacity

    //println("dequeue", this.queue.mkString(","))

    elem
  }
}

