package slickORM

import com.typesafe.config.ConfigFactory
import slickORM.models.tables._

import scala.concurrent.Await
import scala.concurrent.duration._
import slick.driver.MySQLDriver.api._

object monadicQuery extends App {
  ConfigFactory.invalidateCaches()
  val db = Database.forConfig("MySQL")

  try {

    val monadicJoin = for {
      c <- channels
      a <- articles
      bt <- tags
      ct <- tags
      if c.id === 58 && a.id === c.article && c.brand === bt.id && c.channel === ct.id
    } yield (a.title, bt.tagX, ct.tagX)

    println(monadicJoin.result.statements.headOption)

    val rows = Await.result(db.run(monadicJoin.result), 5 seconds)
    rows foreach { row =>
      println(row)
    }
  } catch {
    case e: Throwable => println(e)
  } finally db.close
}
