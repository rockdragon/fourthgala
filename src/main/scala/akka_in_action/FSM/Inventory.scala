package akka_in_action.FSM

import akka.actor.{ActorRef, Actor, FSM}

sealed trait State
case object WaitForRequests extends State
case object ProcessRequest extends State
case object WaitForPublisher extends State
case object SoldOut extends State
case object ProcessSoldOut extends State
case class BookRequest(name: String, actorRef: ActorRef)
case class StateData(nrBooksInStore: Int,
                    pendingRequests: Seq[BookRequest])
case class PendingRequests
class Inventory extends Actor with FSM[State, StateData]{
  startWith(WaitForRequests, new StateData(0, Seq()))

  when(WaitForRequests) {
    case Event(request: BookRequest, data: StateData) => {
      val newStateData = data.copy(pendingRequests = data.pendingRequests :+ request
      )
      if (newStateData.nrBooksInStore > 0) {
        goto(ProcessRequest) using newStateData
      } else {
        goto(WaitForPublisher) using newStateData
      }
    }
    case Event(PendingRequests, data:StateData) => {
      if (data.pendingRequests.isEmpty) {
        stay
      } else if (data.nrBooksInStore > 0) {
        goto(ProcessRequest)
      } else {
        goto(WaitForPublisher)
      }
    }
  }
}
