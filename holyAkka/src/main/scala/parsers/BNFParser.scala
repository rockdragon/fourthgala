package me.moye.practise.parsers.bnfParser

import scala.util.parsing.combinator._

class ExprParser extends RegexParsers {
  def number: Parser[Double] =
    """\d+(\.\d*)?""".r ^^ {
      _.toDouble
    }

  def factor: Parser[Double] = number | "(" ~ expr ~ ")" ^^ {
    case _ ~ e ~ _ => e
  }

  def term: Parser[Double] = factor ~ rep("*" ~ log(factor)("Multiply term")
    | "/" ~ log(factor)("Divide term")
    | "%" ~ log(factor)("Mod term")) ^^ {
    case number ~ list => (number /: list) {
      case (x, "*" ~ y) => x * y
      case (x, "/" ~ y) => x / y
      case (x, "%" ~ y) => x % y
    }
  }

  def expr: Parser[Double] = term ~ rep("+" ~ log(term)("Plus term")
    | "-" ~ log(term)("Minus term")) ^^ {
    case number ~ list => (number /: list) {
      case (x, "+" ~ y) => x + y
      case (x, "-" ~ y) => x - y
    }
  }

  def apply(input: String): Double = parseAll(expr, input) match {
    case Success(result, _) => result
    case failure: NoSuccess => scala.sys.error(failure.msg)
  }
}

object main {
  def main(args: Array[String]): Unit = {
    val parser = new ExprParser()
    val expr = "(1+2)*3%4"
    println(expr + "=" + parser.apply(expr))
  }
}

