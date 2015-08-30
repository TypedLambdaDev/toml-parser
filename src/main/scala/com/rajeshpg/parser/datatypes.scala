package com.rajeshpg.parser


package object datatypes {
  import scala.reflect.ClassTag

  case class TomlData(data: Map[String, Any]) {

    private def getForKey(k: List[String], d: Map[String, Any]): Option[Any] = k match {
      case k :: ks => d.get(k) match {
        case Some(TomlData(d)) => getForKey(ks, d)
        case Some(m: Map[String, Any]) => getForKey(ks, m)
        case d => d
      }
      case Nil => None
    }

    def get(key: String): Option[Any] = {
      getForKey(key.split("[.]").toList, data)
    }

    def getOrElse[A : ClassTag](key: String, default: A): A = optional[A](key).getOrElse(default)

    def require[A : ClassTag](key: String): A = optional[A](key).getOrElse(throw new Exception(s"Key '$key' required but was not found."))

    def optional[A: ClassTag](key: String): Option[A] = get(key) match {
      case Some(value: A) => Some(value)
      case _              => None
    }

    // Shorthand

    def req[A: ClassTag](key: String) = require(key)
    def opt[A: ClassTag](key: String) = optional(key)

    def seq[A: ClassTag](key: String) = optional[Seq[A]](key).getOrElse(Seq.empty)

  }

  object TomlData {
    val empty = TomlData(Map.empty)
  }

  sealed trait Statement
  case class Expression(key: String, value: Any) extends Statement
  case class ExpressionGroup(keys: List[String], value: List[Expression]) extends Statement
}