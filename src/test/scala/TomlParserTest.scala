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

  "TomlParser" should {

    "return name" in {
      data.getAny("name") must beSome("Rajesh")
    }
    
    "return bio" in {
      data.getAny("bio") must beSome("GitHub Cofounder & CEO\nLikes tater tots and beer.")
    }
    
    "return age" in {
       data.getAny("age") must beSome(32)
    }
    
    "return height" in {
      data.getAny("height") must beSome(5.9)
    }
    
    "return list of data" in {
      data.getAny("data") must beSome(List(List("gamma","delta"),List(1,2)))
    }

    "return ip for alpha server" in {
      data.getAny("servers.alpha.ip") must beSome ("10.0.0.1")
    }

    "return ip for beta server" in {
      data.getAny("servers.beta.ip") must beSome ("10.0.0.2")
    }

    "return strings typed as String" in {
      data.get[String]("name") must beSome("Rajesh")
    }

    "return true/false as Boolean" in {
      data.get[Boolean]("developer") must beSome(true)
    }

    "return whole numbers as Int" in {
      data.get[Int]("age") must beSome(32)
    }

    "return rational numbers as Double" in {
      data.get[Double]("height") must beSome(5.9)
    }

    "return lists as typed List" in {
      data.get[List[String]]("admins") must beSome(List("Bob", "John"))
    }

    "return the default when the value is not present" in {
      data.getOrElse("doesntexist", 42) mustEqual 42
    }
    
    "throw an exception when a key is required but not found" in {
      data.require[String]("doesntexist") must throwAn[Exception]
    }

 }

}