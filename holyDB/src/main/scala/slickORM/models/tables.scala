
package slickORM.models

import slick.lifted.TableQuery

object tables {
  val articles = TableQuery[Articles]
  val channels = TableQuery[Channels]
  val tags = TableQuery[Tags]
}
