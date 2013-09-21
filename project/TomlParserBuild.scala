import sbt._
import sbt.Keys._

object TomlParserBuild extends Build {

  val specs2 = "org.specs2" %% "specs2" % "2.2.2" % "test"

  lazy val tomlParser = Project(
    id = "toml-parser",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "toml-parser",
      organization := "com.rajeshpg",
      version := "0.1",
      scalaVersion := "2.10.2",
      libraryDependencies ++= Seq(specs2)
      // add other settings here
    )
  )
}
