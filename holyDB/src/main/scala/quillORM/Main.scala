
package quillORM

import com.typesafe.config.ConfigFactory
import io.getquill._
case class t_shorturl_article(id:Int, title:String)

object Main extends App {
  ConfigFactory.load("")
  val ctx = new JdbcContext[MySQLDialect, SnakeCase]("MySQL")
  import ctx._


  val q = quote {
    query[t_shorturl_article].filter(p => p.id == 51)
  }
  ctx.run(q)
}
