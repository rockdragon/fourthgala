package akka_in_action

import akka.actor.{Props, PoisonPill, Actor}

class TicketSeller extends Actor {

  import TicketProtocol._

  var tickets = Vector[Ticket]()

  def receive = {
    case GetEvents => sender ! tickets.size

    case Tickets(newTickets) =>
      tickets = tickets ++ newTickets

    case BuyTicket =>
      if (tickets.isEmpty) {
        sender ! SoldOut
        self ! PoisonPill
      }

      tickets.headOption.foreach { ticket =>
        tickets = tickets.tail
        sender ! ticket
      }

    case Event(name, nrOrTickets) =>
      if (context.child(name).isEmpty) {
        val ticketSeller = context.actorOf(Props[TicketSeller], name)

        val tickets = Tickets((1 to nrOrTickets).map{
          nr => Ticket(name, nr)
        }.toList)
        ticketSeller ! tickets
      }

      sender ! EventCreated
  }
}
