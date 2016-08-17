package akka_in_action.cluster

import akka.actor.{Actor, ActorLogging}

case class StartJob(jobName: String, text: String)

class JobMaster extends Actor
  with ActorLogging
  with CreateWorkerRouter{

  val router = createWorkerRouter

  def receive = idle

  def idle:Receive = {
    case StartJob(jobName, text) => {

    }
  }
}

class JobWorker extends Actor
  with ActorLogging {
  def receive = {
    case _ =>
  }
}
