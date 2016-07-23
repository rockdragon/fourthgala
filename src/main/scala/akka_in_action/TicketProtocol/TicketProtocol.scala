package akka_in_action.TicketProtocol

case class Event(event: String, nrOfTickets: Int)

case object GetEvents

case class Events(events: List[Event])

case object EventCreated

case class TicketRequest(event: String)

case object SoldOut

case class Tickets(tickets: List[Ticket])

case object BuyTicket

case class Ticket(event: String, nr: Int)