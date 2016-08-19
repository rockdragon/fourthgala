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
      ((c, a), b) <- channels joinLeft
         articles on (_.article === _.id) joinLeft
        tags on (_._1.brand === _.id)

    } yield (c.id, a.map(_.title).getOrElse(""), b.map(_.tagX).getOrElse(""))

    println(monadicJoin.result.statements.headOption)

    val rows = Await.result(db.run(monadicJoin.result), 5 seconds)
    rows foreach { row =>
      println(row)
    }
  } catch {
    case e: Throwable => println(e)
  } finally db.close
}
