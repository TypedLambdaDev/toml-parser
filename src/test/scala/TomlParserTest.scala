import com.rajeshpg.parser.TomlParser

import org.specs2.mutable.Specification

class TomlParserTest extends Specification {
  val parser = new TomlParser
    val data = parser.parse("""
     name = "Rajesh" 
     bio = "GitHub Cofounder & CEO\nLikes tater tots and beer." 
     age=32 
     height= 5.9 
     data = [ ["gamma", "delta"], [1, 2] ] 
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
      println(data.get("data"))
      data.get("data") must beSome(List(List("gamma","delta"),List(1,2))) 
    }

    "return ip for alpha server" in {
      data.get("servers.alpha.ip") must beSome ("10.0.0.1")
    }

    "return ip for beta server" in {
      data.get("servers.beta.ip") must beSome ("10.0.0.2")
    }

 }

}