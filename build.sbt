import sbt.Keys._

name := """fourthgala"""
version := "1.0"
scalaVersion := "2.11.7"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.0"

fork in run := true

lazy val hello = taskKey[Unit]("一个task 示例")

lazy val root = (project in file(".")).
  settings(
    hello := {
      println("Hello!")
    }
  )