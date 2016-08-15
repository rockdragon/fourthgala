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
    val q = links.filter(_.id == 1671423).map(_.pageTitle)

    Await.result(db.run(q.result).map { result =>
        println(result.mkString(","))
      }, 5 seconds
    )

  } catch {
    case e => println(e)
  } finally db.close

  Thread.sleep(20000)
}
