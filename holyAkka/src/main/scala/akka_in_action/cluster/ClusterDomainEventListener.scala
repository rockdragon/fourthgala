package akka_in_action.cluster

import akka.actor.{Actor, ActorLogging}
import akka.cluster.{Cluster, MemberStatus}
import akka.cluster.ClusterEvent._

class ClusterDomainEventListener extends Actor
  with ActorLogging {

  Cluster(context.system).subscribe(self, classOf[ClusterDomainEvent])

  def receive = {
    case MemberUp(member) => log.info(s"$member UP.")
    case MemberExited(member) => log.info(s"$member EXITED")
    case MemberRemoved(member, previousStatus) => {
      if(previousStatus == MemberStatus.exiting) {
        log.info(s"Member $member gracefully exited, REMOVED")
      } else {
        log.info(s"$member downed after unreachable, REMOVED")
      }
    }
    case UnreachableMember(member) => log.info(s"$member UNREACHABLE")
    case state: CurrentClusterState => log.info(s"Cluster state: $state")
  }

  override def postStop(): Unit = {
    Cluster(context.system).unsubscribe(self)
    super.postStop()
  }
}
