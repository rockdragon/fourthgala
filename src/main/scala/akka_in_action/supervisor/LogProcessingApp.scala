package akka_in_action.supervisor

import akka.actor.{Actor, Props, ActorSystem}

case class DbCon(conn: String)
class DbWriter(dbCon: DbCon) extends Actor { def receive = { case _ => } }
class DbSupervisor(props: Props) extends Actor { def receive = { case _ => } }
class DbWriter extends Actor { def receive = { case _ => } }
class LogProcSupervisor(props: Props) extends Actor { def receive = { case _ => } }
class FileWatchingSupervisor(sources: Vector[String], props: Props) extends Actor { def receive = { case _ => } }

object LogProcessingApp extends App {
  val sources = Vector("file:///source1/", "file:///source2/")
  val system = ActorSystem("logprocessing")
  // create the props and dependencies
  val con = new DbCon("http://mydatabase")
  val writerProps = Props(new DbWriter(con))
  val dbSuperProps = Props(new DbSupervisor(writerProps))
  val logProcSuperProps = Props(
    new LogProcSupervisor(dbSuperProps))
  val topLevelProps = Props(new FileWatchingSupervisor(
    sources,
    logProcSuperProps))
  system.actorOf(topLevelProps)
}