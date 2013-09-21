package com.rajeshpg.parser

package object datatypes {
  case class TomlData(data: Map[String, Any]) {

    def getForKey(k: List[String], d: Map[String, Any]): Option[Any] = k match {

      case k :: ks => d.get(k) match {
        case Some(TomlData(d)) => getForKey(ks, d)
        case Some(m: Map[String, Any]) => ; getForKey(ks, m)
        case d => d
      }

      case Nil => None

    }

    def get(key: String) = {
      getForKey(key.split("[.]").toList, data)
    }

  }
  object TomlData {
    val empty = TomlData(Map())
  }
  sealed trait Statement
  case class Expression(key: String, value: Any) extends Statement
  case class ExpressionGroup(keys: List[String], value: List[Expression]) extends Statement
}