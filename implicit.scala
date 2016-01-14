package com.example.implicitexp

object com {
        implicit def str2int(s: String): Int = java.lang.Integer.parseInt(s)
}

object Main {
    import com.str2int
    
    def foo(a: Int) : Int = a + 10
    
    def main(args: Array[String]):Unit = {
        val result = foo("123")
        println(result)
    }
}
