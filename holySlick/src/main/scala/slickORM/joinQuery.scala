package slickORM

import com.typesafe.config.ConfigFactory
import slick.driver.MySQLDriver.api._
import slickORM.models.tables._

import scala.concurrent.Await
import scala.concurrent.duration._

object joinQuery extends App {
  ConfigFactory.invalidateCaches()
  val db = Database.forConfig("MySQL")

  try {

    val monadicJoin = for {
      ((a, c), b) <- articles joinLeft channels on (_.id === _.article) joinLeft
        tags on (_._2.map(_.brand) === _.id)

    } yield (c.map(_.id).getOrElse(0), a.title, b.map(_.tagX).getOrElse(""))

    println(monadicJoin.result.statements.headOption)

    val rows = Await.result(db.run(monadicJoin.result), 5 seconds)
    rows foreach { row =>
      println(row)
    }
  } catch {
    case e: Throwable => println(e)
  } finally db.close
}
