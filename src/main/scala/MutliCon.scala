package me.moye.holy

object Address {
  def zipToCity(zip: String) = "AnyTown"

  def zipToState(zip: String) = "CA"
}

case class Address(street: String, city: String, state: String, zip: String) {
  def this(zip: String) =
    this("[unknown]", Address.zipToCity(zip), Address.zipToState(zip), zip)
}

case class Person(
                   name: String,
                   age: Option[Int] = None,
                   address: Option[Address] = None
                 )

object Person {
  def apply(name: String) = new Person(name)

  def apply(name: String, age: Int) = new Person(name, Some(age))

  def apply(name: String, age: Int, address: Address) = new Person(name, Some(age), Some(address))

  def apply(name: String, address:Address) = new Person(name, address = Some(address))
}

object main {
  def main(args:Array[String]): Unit = {
    val a2 = new Address("1 scala name", "AnyTown", "AC", "8834")

    println(Person("Buck street"))

    println(Person("Buck street 22", 22))

    println(Person("Buck street 333", a2))
  }
}