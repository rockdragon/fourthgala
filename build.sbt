import sbt.Keys._

name := """fourthgala"""
version := "1.0"
scalaVersion := "2.11.8"

autoCompilerPlugins := true
addCompilerPlugin(
  "org.scala-lang.plugins" % "scala-continuations-plugin_2.11.8" % "1.0.2")


libraryDependencies ++= {
  val akkaVersion = "2.3.2"
  val sprayVersion = "1.3.3"
  val scalazVersion = "7.1.0"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-client" % sprayVersion,
    "io.spray" %%  "spray-json" % "1.3.2",
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "org.scalaz" %% "scalaz-core" % scalazVersion,
    "org.scalaz" %% "scalaz-effect" % scalazVersion,
    "org.scalaz" %% "scalaz-typelevel" % scalazVersion,
    "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test",
    "org.scalatest" %% "scalatest" % "2.2.4",
    "org.scala-lang.plugins" %% "scala-continuations-library" % "1.0.2"
  )
}

scalacOptions += "-P:continuations:enable"

fork in run := true