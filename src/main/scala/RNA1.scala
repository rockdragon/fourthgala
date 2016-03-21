import collection.IndexedSeqLike
import collection.mutable.{Builder, ArrayBuffer}
import collection.generic.CanBuildFrom

abstract class Base

case object A extends Base

case object T extends Base

case object G extends Base

case object U extends Base

object Base {
  val fromInt: Int => Base = Array(A, T, G, U)
  val toInt: Base => Int = Map(A -> 0, T -> 1, G -> 2, U -> 3)
}

final class RNA1 private(val groups: Array[Int],
                         val length: Int) extends IndexedSeq[Base] {

  import RNA1._

  def apply(idx: Int): Base = {
    if (idx < 0 || length <= idx)
      throw new IndexOutOfBoundsException
    Base.fromInt(groups(idx / N) >> (idx % N * S) & M)
  }
}

object RNA1 {
  // 表示一组所需要的比特数
  private val S = 2
  // 一个Int能够放入的组数
  private val N = 32 / S
  // 分离组的位掩码(bitmask)
  private val M = (1 << S) - 1

  def fromSeq(buf: Seq[Base]): RNA1 = {
    val groups = new Array[Int]((buf.length + N - 1) / N)
    for (i <- buf.indices)
      groups(i / N) |= Base.toInt(buf(i)) << (i % N * S)
    new RNA1(groups, buf.length)
  }

  def apply(bases: Base*) = fromSeq(bases)
}

final class RNA2 private(
                        val groups: Array[Int],
                        val legngth :Int
                        ) extends IndexedSeqLike[Base, RNA2] {
  import RNA2._

  override def newBuilder: Builder[Base, RNA2] =
    new ArrayBuffer[Base] mapResult fromSeq

  def apply(bases: Base*) = fromSeq(bases)
}

object RNA2 {

}
