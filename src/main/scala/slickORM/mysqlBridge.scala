package slickORM

import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

object mysqlBridge extends App {
  ConfigFactory.invalidateCaches()
  val db = Database.forConfig("MySQL")

  try {
    val links = TableQuery[Links]

    Await.ready(db.run(links.result).map(_.foreach { row =>
      println("~~~~~~~~~", row)
    }), 5 seconds)

  } catch {
    case e => println(e)
  } finally db.close

  Thread.sleep(10000)
}
