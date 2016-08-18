package slickORM

import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._
import slick.driver.MySQLDriver.api._

object mysqlBridge extends App {
  ConfigFactory.invalidateCaches()
  val db = Database.forConfig("MySQL")

  try {
    val articles = TableQuery[Articles]

    val query = for {
      c <- articles if c.id === 49
    } yield c

    val rows = Await.result(db.run(query.result), 5 seconds)
    rows foreach { row =>
      println(row)
    }

  } catch {
    case e: Throwable => println(e)
  } finally db.close
}
