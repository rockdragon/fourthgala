
package slickORM

import com.typesafe.config.ConfigFactory
import slick.driver.MySQLDriver.api._
import slick.jdbc.GetResult

import scala.concurrent.Await
import scala.concurrent.duration._

object rawSQLQuery extends App {
  ConfigFactory.invalidateCaches()
  val config = ConfigFactory.load("jdbc")
  val db = Database.forConfig("MySQLRaw", config)
  try {
    case class AssembledObject(cid: Int, aid: Int, channel: String, brand: String, title: String)
    implicit val getAssembledObjectResult = GetResult(r => AssembledObject(r.nextInt, r.nextInt,
      r.nextString, r.nextString, r.nextString))

    val rawSQL = sql"""select c.id as cid, a.id as aid, g.tag as channel, g2.tag as brand,
                              a.title from t_shorturl_channel as c
                      	  inner join t_shorturl_article as a
                      	       on c.article_id = c.article_id
                          inner join generic_tag as g
                              on c.channel_id = g.id
                          inner join generic_tag as g2
                              on c.brand_id = g2.id
                          where c.id = 58""".as[AssembledObject]

    val rows = Await.result(db.run(rawSQL), 15 seconds)
    rows foreach { row =>
      println(row)
    }
  } catch {
    case e: Throwable => println(e)
  } finally db.close
}
