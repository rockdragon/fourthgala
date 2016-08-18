package slickORM

import java.sql.Date
import slick.driver.MySQLDriver.api._
import slick.lifted.{ProvenShape}
import slick.ast.ColumnOption.PrimaryKey

class Articles(tag: Tag) extends Table[(Int, String, Int, Int, String, Int, Date, Date)](tag, "t_shorturl_article") {
  def id = column[Int]("id", PrimaryKey)
  def title = column[String]("title")
  def adminId = column[Int]("admin_id")
  def topicId = column[Int]("topic_id")
  def url = column[String]("url")
  def status = column[Int]("status")
  def updatedTime = column[Date]("utime")
  def creationDate = column[Date]("ctime")

  def * :ProvenShape[(Int, String, Int, Int, String, Int, Date, Date)] =
    (id, title, adminId, topicId, url, status, updatedTime, creationDate)
}
