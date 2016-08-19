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
    val channels = TableQuery[Channels]
    val tags = TableQuery[Tags]

    val monadicJoin = for {
      a <- articles
      c <- channels
      bt <- tags
      ct <- tags
      if a.id === c.article && c.brand === bt.id && c.channel === ct.id && a.id === 48
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
