package akka_in_action.standalone

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem}

import scala.concurrent.duration._

class HelloWorldCaller(timer: FiniteDuration, actor: ActorRef)
  extends Actor with ActorLogging {
  implicit val system = ActorSystem("holy")
  import system.dispatcher

  case class TimerTick(msg: String)

  override def preStart() {
    super.preStart()
    context.system.scheduler.schedule(
      timer,
      timer,
      self,
      TimerTick("everybody"))
  }

  def receive = {
    case msg: String => log.info("received {}", msg)
    case tick: TimerTick => actor ! tick.msg
  }
}
