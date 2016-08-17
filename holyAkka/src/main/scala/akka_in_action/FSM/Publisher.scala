package akka_in_action.FSM

import akka.actor.Actor
import scala.math.min

class Publisher(totalNrBooks: Int, nrBooksPerRequest: Int)
  extends Actor {

  var nrLeft = totalNrBooks

  def receive = {
    case PublisherRequest => {
      if(nrLeft == 0)
        sender ! BookSupplySoldOut
      else {
        val supply = min(nrBooksPerRequest, nrLeft)
        nrLeft -= supply
        sender ! new BookSupply(supply)
      }
    }
  }
}
