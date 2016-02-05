package me.moye.poly

case class Address(street: String, city: String, state: String, zip: String)

object Address {
  def apply(zip: String) = new Address(
    "[unknown]", Address.zipToCity(zip), Address.zipToState(zip), zip)

  def zipToCity(zip: String) = "AnyTown"

  def zipToState(zip: String) = "CA"
}

trait PersonState {
  val name: String
  val age: Option[Int]
  val address: Option[Address]
}

case class Person(
                   name: String,
                   age: Option[Int] = None,
                   address: Option[Address] = None
                 ) extends PersonState

trait EmployeeState {
  val title: String
  val manager: Option[Employee]
}

case class Employee(
                   name:String,
                   age:Option[Int] = None,
                   address: Option[Address] = None,
                   title: String = "[unknown]",
                   manager: Option[Employee] = None
                   ) extends PersonState with EmployeeState

object main {
  def main(args: Array[String]): Unit = {
    val ceoAddress = Address("1 Scala Lane", "Anytown", "CA", "98765")

    val buckAddress = Address("98765")
    val ceo = Employee(
      name = "Joe CEO", title = "CEO", age = Some(50),
      address = Some(ceoAddress), manager = None)

    val ceoSpouse = Person("Jane Smith", address = Some(ceoAddress))

    val buck = Employee(
      name = "Buck Trends", title = "Zombie Dev", age = Some(20),
      address = Some(buckAddress), manager = Some(ceo))


   println(Person("Ann Collins", address = Some(buckAddress)))
  }
}