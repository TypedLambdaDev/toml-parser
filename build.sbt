name := "toml-parser"

organization := "com.rajeshpg"

version := "0.1"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq ("org.specs2" %% "specs2" % "2.3.11" % "test",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1")
