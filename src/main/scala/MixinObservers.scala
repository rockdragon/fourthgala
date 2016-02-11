package me.moye.mixins

abstract class Widget

trait Observer[-State] {
  def receiveUpdate(state: State): Unit
}

trait Subject[State] {
  private var observers: List[Observer[State]] = Nil

  def addObserver(observer: Observer[State]): Unit =
    observers ::= observer //observers = observer :: observers

  def notifyObservers(state: State) =
    observers.foreach(_.receiveUpdate(state))
}

class Button(val label: String) extends Widget with Clickable {
  def updateUI(): Unit = {
    /*..*/
  }
}

class ObservableButton(name: String)
  extends Button(name) with Subject[Button] {

  override def click(): Unit = {
    super.click()
    notifyObservers(this)
  }
}

class ButtonCountObserver extends Observer[Button] {
  var count = 0

  def receiveUpdate(state: Button): Unit = count += 1
}

trait Clickable {
  def click(): Unit = updateUI()

  protected def updateUI(): Unit
}

trait ObservableClicks extends Clickable with Subject[Clickable] {
  abstract override def click(): Unit = {
    super.click()
    notifyObservers(this)
  }
}

class ClickCountObserver extends Observer[Clickable] {
  var count = 0

  def receiveUpdate(state: Clickable): Unit = count += 1
}

trait VetoableClicks extends Clickable {
  val maxAllowed = 1
  private var count = 0

  abstract override def click() = {
    if (count < maxAllowed) {
      count += 1
      super.click()
    }
  }
}

object main {
  def main(args: Array[String]): Unit = {
    val button = new Button("Click Me!") with ObservableClicks with VetoableClicks {
      override val maxAllowed = 2
    }

    val bco1 = new ClickCountObserver
    val bco2 = new ClickCountObserver

    button addObserver bco1
    button addObserver bco2

    (1 to 5) foreach (_ => button.click)

    println(bco1.count, bco2.count)
  }

}

