package me.moye.db

object Database {
  case class ResultSet(/*...*/)
  case class Connection(/*...*/)

  case class DatabaseException(message:String, cause:Throwable) extends
    RuntimeException(message, cause)

  sealed trait Status
  case object Disconnected extends Status
  case class Connected(connected: Connection) extends Status
  case class QuerySucceeded(result: ResultSet) extends Status
  case class QueryFailed(e: DatabaseException) extends Status
}

class Database {
  import me.moye.db.Database._

  def connect(server:String): Status = ???
  def disconnect(): Status = ???

  def query(/*...*/): Status = ???
}
