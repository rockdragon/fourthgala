
package quillORM

import com.typesafe.config.ConfigFactory
import io.getquill._
case class t_shorturl_article(id:Int, title:String)

object Main extends App {
  ConfigFactory.invalidateCaches()
  val conf = ConfigFactory.load("quill").getConfig("ctx")

  val ctx = new JdbcContext[MySQLDialect, SnakeCase](conf)
  import ctx._

  val q = quote {
    query[t_shorturl_article].filter(p => p.id == 51)
  }
  val article = ctx.run(q)
  println(article)
}
