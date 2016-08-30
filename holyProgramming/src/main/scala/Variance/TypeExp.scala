
package Variance

object TypeExp extends App {
  class Covariant[+A]
  val vc: Covariant[AnyRef] = new Covariant[String]
//  val vc2: Covariant[String] = new Covariant[AnyRef]

  class Contravariance[-A]
  val cvc: Contravariance[String] = new Contravariance[AnyRef]
//  val cvc2: Contravariance[AnyRef] = new Contravariance[String]
}
