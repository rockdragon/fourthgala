package akka_in_action.STM

import scala.concurrent.stm.atomic
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.duration._

case class Withdraw(amount:Int)
case class Deposit(amount:Int)

case class TransferMessage(amount: Int,
                           from: ActorRef,
                           to: ActorRef)

class Account(name: String, var balance: Int) extends Actor {
  override def receive: Receive = {
    case Withdraw(amount) => {
      if(this.balance > amount) {
        this.balance -= this.balance
        println(s"${name} withdraw: ${amount}, balance: ${balance}")
      } else throw new Exception("insufficient balance")
    }
    case Deposit(amount) => {
      this.balance += amount
      println(s"${name} deposit: ${amount}, balance: ${balance}")
    }
  }
}

class Transfer extends Actor {
  implicit val timeout = new Timeout(1 seconds)

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    message.foreach(_ => sender ! "Failed")
    super.preRestart(reason, message)
  }

  override def receive: Receive = {
    case TransferMessage(amount, from, to) => {
       atomic { implicit tnx => {
        from ! Withdraw(amount)
        to ! Deposit(amount)
      }}
    }
  }
}

object TransferTransaction extends App {
  implicit val system = ActorSystem("actor-system")
  import system.dispatcher

  val transfer = system.actorOf(Props(new Transfer), "transfer")
  val from = system.actorOf(Props(new Account("from", 60)))
  val to = system.actorOf(Props(new Account("to", 20)))

  transfer ! TransferMessage(70, from, to)

}
