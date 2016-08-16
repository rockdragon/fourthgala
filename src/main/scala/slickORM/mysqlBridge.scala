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

    val query = for {
      c <- links if c.id === 1671361L
    } yield c

    Await.ready(db.stream(query.result).foreach { row =>
      println("~~~~~~~~~", row)
    }, 5 seconds)

  } catch {
    case e => println(e)
  } finally db.close

  Thread.sleep(2000)
}
