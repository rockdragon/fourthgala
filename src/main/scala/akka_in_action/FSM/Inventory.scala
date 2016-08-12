package akka_in_action.FSM

import akka.actor.{ActorRef, Actor, FSM}

sealed trait State
case object WaitForRequests extends State
case object ProcessRequest extends State
case object WaitForPublisher extends State
case object SoldOut extends State
case object BookSupplySoldOut extends State
case object ProcessSoldOut extends State
case object PublisherRequest extends State
case class BookRequest(name: String, actorRef: ActorRef)
case class StateData(nrBooksInStore: Int,
                    pendingRequests: Seq[BookRequest])
case class BookSupply(qty: Int)
case class PendingRequests()

class Inventory(publisher: ActorRef) extends Actor with FSM[State, StateData]{
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

  whenUnhandled {
    case Event(request: BookRequest, data: StateData) => {
      stay using data.copy(
        pendingRequests = data.pendingRequests :+ request)
    }
    case Event(e, s) => {
      log.warning("received unhandled request {} in state {}/{}",
        e, stateName, s)
      stay
    }
  }

  onTransition {
    case _ -> WaitForRequests => {
      if (!nextStateData.pendingRequests.isEmpty) {
        self ! PendingRequests
      }
    }
    case _ -> WaitForPublisher => {
      publisher ! PublisherRequest
    }
  }

  onTermination {
    case StopEvent(FSM.Normal, state, data) =>
    case StopEvent(FSM.Shutdown, state, data) =>
    case StopEvent(FSM.Failure(e), state, data) =>
  }
}
