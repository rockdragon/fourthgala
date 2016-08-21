
package cps

import scala.util.continuations._
import java.io._

object PriintFiles extends App {
  var cont: (Unit => Unit) = null

  def processDirectory(dir: File): Unit @cps[Unit] = {
    val files = dir.listFiles
    var i = 0
    while (i < files.length) {
      val f = files(i)
      i += 1

      if(f.isDirectory) {
        processDirectory(f)
      } else {
        shift {
          k: (Unit => Unit) => {
            cont = k
          }
        }
        println(f) // scalastyle:ignore
      }
    }
  }

  reset {
    processDirectory(new File("/Users/moye"))
  }

  for(i <- 1 to 2) cont()
}

