package com.rajeshpg.parser

import scala.reflect.ClassTag


package object datatypes {

  case class TomlData(data: Map[String, Any]) {

    def getForKey(k: List[String], d: Map[String, Any]): Option[Any] = k match {
      case k :: ks => d.get(k) match {
        case Some(TomlData(d)) => getForKey(ks, d)
        case Some(m: Map[String, Any]) => getForKey(ks, m)
        case d => d
      }
      case Nil => None
    }

    def getAny(key: String): Option[Any] = {
      getForKey(key.split("[.]").toList, data)
    }

    def get[A: ClassTag](key: String): Option[A] = getAny(key) match {
      case Some(value: A) => Some(value)
      case _              => None
    }

    def require[A : ClassTag](key: String): A = get[A](key).getOrElse(throw new Exception(s"Key '$key' required but was not found."))

    def getOrElse[A : ClassTag](key: String, default: A): A = get[A](key).getOrElse(default)

  }

  object TomlData {
    val empty = TomlData(Map.empty)
  }

  sealed trait Statement
  case class Expression(key: String, value: Any) extends Statement
  case class ExpressionGroup(keys: List[String], value: List[Expression]) extends Statement
}