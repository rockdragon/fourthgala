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

class Button(val label: String) extends Widget {
  def click(): Unit = updateUI()

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

object main {
  def main(args: Array[String]): Unit = {
    val button = new ObservableButton("Click me")
    val bco1 = new ButtonCountObserver
    val bco2 = new ButtonCountObserver

    button addObserver bco1
    button addObserver bco2

    (1 to 5) foreach (_ => button.click)

    println(bco1.count, bco2.count)
  }

}

