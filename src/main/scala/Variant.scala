class CSuper {
  def msuper() = println("CSuper")
}

class C extends CSuper {
  def m() = println("C")
}

class CSub extends C {
  def msub() = println("CSub")
}

//class ContainerPlus[+A](var a: A) {
//  private var _value: A = a
//  def value_=(newA: A): Unit = _value = newA
//  def value: A = _value
//}

//class ContainerMinus[-A](var a: A) {
//  private var _value: A = a
//  def value_=(newA:A): Unit = _value = newA
//  def value:A = _value
//}

object main {
  def main(args: Array[String]): Unit = {
    var f: C => C = (c: C) => new C
    f = (c: CSuper) => new CSub
    f = (c: CSuper) => new C
    f = (c: C) => new CSub
//    f = (c: CSub) => new CSuper

//    val cp:ContainerPlus[C] = new ContainerPlus(new CSub)
//    cp.value = new C
//    cp.value = new CSub
//    cp.value = new CSuper

//    val cm:ContainerMinus[C] = new ContainerMinus(new CSuper)
//    val c:C = cm.value
//    val c:CSuper = cm.value
//    val c2:CSub = cm.value
  }
}