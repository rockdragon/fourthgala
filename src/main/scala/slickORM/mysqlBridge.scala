package slickORM

import slick.driver.MySQLDriver.api._

object mysqlBridge extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  val db = Database.forConfig("databaseUrl")

}
