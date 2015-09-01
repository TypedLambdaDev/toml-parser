package com.rajeshpg.parser


import datatypes._

class TomlParser  extends TomlGrammar {

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

  private def updateData(tomlData: TomlData, keyList: List[String], value: List[Expression]): TomlData = {
    keyList match {
      case Nil => tomlData
      case key :: tail => tomlData.data.getOrElse(key, TomlData.empty) match {
        case td: TomlData => build(key, tomlData, value, tail, td)
        case m => throw new Exception(s"key $key already exists")
      }
    }
  }

  def build(key: String, tomlData: TomlData, value: List[Expression], tail: List[String], td: TomlData): TomlData = {
    val tomlValue = if (tail.isEmpty && value.nonEmpty)
      value.map(exp => (exp.key, exp.value)).toMap
    else
      updateData(td, tail, value)

    TomlData(tomlData.data.updated(key, tomlValue))
  }

}
