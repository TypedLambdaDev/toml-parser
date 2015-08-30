package com.rajeshpg.parser

import scala.util.parsing.combinator._

import datatypes._
import javax.xml.bind.DatatypeConverter

trait TomlGrammar extends JavaTokenParsers {

  override protected val whiteSpace = """(\s|#.*)+""".r

  val int = """-?\d+""".r ^^ (i => i.toInt)

  val float = """-?\d*[.]\d*""".r ^^ (f => f.toDouble)

  val number = int ||| float

  val boolean = ("true" | "false") ^^ (bool => bool.toBoolean)

  val string = stringLiteral  ^^ (s => s.replace("\"","").replace("\\\"", "\"").replace("\\t", "\t").replace("\\n", "\n").replace("\\\\", "\\").replace("\\r", "\r").replace("\\f", "\f"))

  val date = """\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}Z""".r ^^ (d => DatatypeConverter.parseDateTime(d).getTime())

  val primitives: Parser[Any] = number | boolean | string | array | date

  val array = "[" ~> repsep(primitives, ",") <~ ",".? ~ "]" ^^ (arr => arr.toList)

  val key = """[\p{L}][_\p{L}\p{Nd}]*""".r ^^ (k => k.toString)

  val keyGroup = "[" ~> repsep(key, ".") <~ "]" ^^ (kg => kg.toList)

  val expression = (key <~ "=") ~ primitives ^^ { case k ~ v => Expression(k, v) }

  val expressionGroup = keyGroup ~ expression.* ^^ { case k ~ v => ExpressionGroup(k, v) }

  val statement = expression | expressionGroup

  val statements = statement.*

}
