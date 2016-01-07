package com.example
import scala.util.control.NonFatal

object App {
        def calc(x:Int, y:Int) : Int = {
                x + y
        }

        def holy : Unit = {
                try {
                        println(1 / 0)
                } catch {
                        case NonFatal(ex) => println(s"Non Fatal exception! $ex")
                } finally {
                        println("BYE!")
                }
        }
}
