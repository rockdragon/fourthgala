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

    val rows = Await.result(db.run(query.result), 5 seconds)
    rows foreach { row =>
      println(row)
    }

  } catch {
    case e: Throwable => println(e)
  } finally db.close
}
