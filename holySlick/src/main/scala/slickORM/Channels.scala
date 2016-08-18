package slickORM

import java.sql.Date
import slick.driver.MySQLDriver.api._
import slick.lifted._
import slick.ast.ColumnOption.PrimaryKey

class Channels(tag: Tag) extends Table[(Int, Int, Int, Int, String, Int, Date, Date)](tag, "t_shorturl_channel") {
  def id = column[Int]("id", PrimaryKey)
  def article = column[Int]("article_id")
  def channel = column[Int]("channel_id")
  def brand = column[Int]("brand_id")
  def url = column[String]("short_url")
  def status = column[Int]("status")
  def creationDate = column[Date]("ctime")
  def updatedTime = column[Date]("utime")

  def * :ProvenShape[(Int, Int, Int, Int, String, Int, Date, Date)] =
    (id, article, channel, brand, url, status, creationDate, updatedTime)
}