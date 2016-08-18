package slickORM

import java.sql.Date
import slick.driver.MySQLDriver.api._
import slick.lifted._
import slick.ast.ColumnOption.PrimaryKey

class Tags(tag: Tag) extends Table[(Int, String, String, String, Boolean, Date, Date, Int)](tag, "generic_tag") {
  def id = column[Int]("id", PrimaryKey)
  def tagX = column[String]("tag")
  def typeX = column[String]("type")
  def module = column[String]("module")
  def status = column[Boolean]("status")
  def creationDate = column[Date]("created")
  def updatedTime = column[Date]("updated")
  def parentId = column[Int]("parent_id")

  def * :ProvenShape[(Int, String, String, String, Boolean, Date, Date, Int)] =
    (id, tagX, typeX, module, status, creationDate, updatedTime, parentId)
}