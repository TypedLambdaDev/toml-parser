import sbt._
import sbt.Keys._

object TomlParserBuild extends Build {

  val specs2 = "org.specs2" %% "specs2" % "2.3.11" % "test"
  val parserCombinators = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1"

  lazy val tomlParser = Project(
    id = "toml-parser",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "toml-parser",
      organization := "com.rajeshpg",
      version := "0.1",
      scalaVersion := "2.11.2",
      libraryDependencies ++= Seq(specs2, parserCombinators)
    )
  )
}
