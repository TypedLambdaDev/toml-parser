package com.rajeshpg.parser

import scala.util.parsing.combinator._

import datatypes._
import javax.xml.bind.DatatypeConverter

class TomlParser extends JavaTokenParsers {

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

  def parse(str: String) = {
    val parsed = parseAll(statements, str)
    parsed.get.foldLeft(TomlData.empty)({ buildTomlData })
  }

  private def buildTomlData(tomlData: TomlData, statement: Statement): TomlData = statement match {
    case Expression(k, v) => tomlData.data.get(k) match {
      case None => TomlData(tomlData.data.updated(k, v))
      case Some(_) => throw new Exception("dup key")
    }
    case ExpressionGroup(keyList, expressions) => updateData(tomlData, keyList, expressions)
  }

  private def updateData(tomlData: TomlData, keyList: List[String], value: List[Expression]): TomlData = keyList match {
    case Nil => tomlData
    case key :: tail => tomlData.data.get(key).getOrElse(TomlData.empty) match {
      case td: TomlData => TomlData(tomlData.data.updated(key, if (tail.isEmpty && !value.isEmpty) value.map(exp => (exp.key, exp.value)).toMap else updateData(td, tail, value)))
      case m => throw new Exception(s"key $key already exists")
    }
  }

}
