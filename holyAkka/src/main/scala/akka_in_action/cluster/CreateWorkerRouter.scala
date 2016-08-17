package akka_in_action.cluster

import akka.actor.{Actor, ActorRef, Props}
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.BroadcastPool

trait CreateWorkerRouter {
  this: Actor =>

  def createWorkerRouter: ActorRef = {
    context.actorOf(
      ClusterRouterPool(BroadcastPool(10),
        ClusterRouterPoolSettings(
          totalInstances = 1000,
          maxInstancesPerNode = 20,
          allowLocalRoutees = false,
          useRole = None
        )
      ).props(Props[JobWorker]),
      name = "worker-router")
  }

}
