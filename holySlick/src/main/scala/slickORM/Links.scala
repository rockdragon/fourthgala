package slickORM

import java.sql.Date
import slick.driver.MySQLDriver.api._
import slick.lifted.{ProvenShape}
import slick.ast.ColumnOption.PrimaryKey

class Links(tag: Tag) extends Table[(Long, String, String, Long, String, Date, String, Date)](tag, "LINKS") {
  def id = column[Long]("LINKID", PrimaryKey)
  def pageTitle = column[String]("DESTPAGETITLE")
  def pageKey = column[String]("DESTSPACEKEY")
  def contentId = column[Long]("CONTENTID")
  def creator = column[String]("CREATOR")
  def creationDate = column[Date]("CREATIONDATE")
  def lastModifier = column[String]("LASTMODIFIER")
  def lastModifiedDate = column[Date]("LASTMODDATE")

  def * :ProvenShape[(Long, String, String, Long, String, Date, String, Date)] =
    (id, pageTitle, pageKey, contentId, creator, creationDate, lastModifier, lastModifiedDate)
}