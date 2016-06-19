import sbt.Keys._

name := """fourthgala"""
version := "1.0"
scalaVersion := "2.11.8"

autoCompilerPlugins := true
addCompilerPlugin(
  "org.scala-lang.plugins" % "scala-continuations-plugin_2.11.8" % "1.0.2")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.0"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.7"
libraryDependencies += "org.scala-lang.plugins" %% "scala-continuations-library" % "1.0.2"

scalacOptions += "-P:continuations:enable"

fork in run := true