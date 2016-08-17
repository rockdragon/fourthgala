package algorithms

import org.scalatest._

class RingBufferSpec extends FunSpec with Matchers {

  describe("A RingBuffer") {
    val ringBuffer = new RingBuffer[Int](3)

    it("should be fixed-size") {
      var size = ringBuffer.enqueue(1)
      size should be (1)
      size = ringBuffer.enqueue(2)
      size should be (2)
      size = ringBuffer.enqueue(3)
      size should be (3)
      size = ringBuffer.enqueue(4)
      size should be (3)
    }

    it("should be a circular queue") {
      var elem = ringBuffer.dequeue
      elem should be (2)
      elem = ringBuffer.dequeue
      elem should be (3)
      elem = ringBuffer.dequeue
      elem should be (4)
    }
  }
}
