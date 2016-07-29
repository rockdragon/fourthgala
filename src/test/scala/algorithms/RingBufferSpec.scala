package algorithms

import org.scalatest._

class RingBufferSpec extends FlatSpec with Matchers {

  "A RingBuffer" should "be a circular queue" in {
    val ringBuffer = new RingBuffer[Int](3)

    var size = ringBuffer.enqueue(1)
    size should be (1)
    size = ringBuffer.enqueue(2)
    size should be (2)
    size = ringBuffer.enqueue(3)
    size should be (3)
    size = ringBuffer.enqueue(4)
    size should be (3)

    var elem = ringBuffer.dequeue
    elem should be (2)
    elem = ringBuffer.dequeue
    elem should be (3)
    elem = ringBuffer.dequeue
    elem should be (4)
  }

}
