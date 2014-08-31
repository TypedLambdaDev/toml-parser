import com.rajeshpg.parser.TomlParser

import org.specs2.mutable.Specification

class TomlParserTest extends Specification {
  val parser = new TomlParser
    val data = parser.parse("""
     name = "Rajesh" 
     bio = "GitHub Cofounder & CEO\nLikes tater tots and beer."
     age=32
     height= 5.9
     developer = true
     data = [ ["gamma", "delta"], [1, 2] ]
     admins = [ "Bob", "John" ]

     [servers]
      [servers.alpha]
      ip = "10.0.0.1"
      dc = "eqdc10"
      [servers.beta]
      ip = "10.0.0.2"
      dc = "eqdc10"

      """)

  "#get" should {

    "return name" in {
      data.get("name") must beSome("Rajesh")
    }

    "return bio" in {
      data.get("bio") must beSome("GitHub Cofounder & CEO\nLikes tater tots and beer.")
    }

    "return age" in {
      data.get("age") must beSome(32)
    }

    "return height" in {
      data.get("height") must beSome(5.9)
    }

    "return list of data" in {
      data.get("data") must beSome(List(List("gamma", "delta"), List(1, 2)))
    }

    "return ip for alpha server" in {
      data.get("servers.alpha.ip") must beSome("10.0.0.1")
    }

    "return ip for beta server" in {
      data.get("servers.beta.ip") must beSome("10.0.0.2")
    }
  }

  "#opt" should {

    "return strings typed as String" in {
      data.opt[String]("name") must beSome("Rajesh")
    }

    "return true/false as Boolean" in {
      data.opt[Boolean]("developer") must beSome(true)
    }

    "return whole numbers as Int" in {
      data.opt[Int]("age") must beSome(32)
    }

    "return rational numbers as Double" in {
      data.opt[Double]("height") must beSome(5.9)
    }

    "return lists as typed List" in {
      data.opt[List[String]]("admins") must beSome(List("Bob", "John"))
    }

  }

  "#getOrElse" should {

    "return the default when the value is not present" in {
      data.getOrElse("doesntexist", 42) mustEqual 42
    }

  }

  "#require" should {

    "throw an exception when a key is required but not found" in {
      data.req[String]("doesntexist") must throwAn[Exception]
    }

  }

  "#seq" should {

    "return a list" in {
      data.seq[String]("admins") mustEqual List("Bob", "John")
    }

    "return an empty list if not found" in {
      data.seq[String]("doesntexist") mustEqual List.empty
    }

 }

}