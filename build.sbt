import sbt.Keys._

lazy val commonSettings = Seq(
  name := "fourthgala",
  version := "1.0",
  scalaVersion := "2.11.8",
  autoCompilerPlugins := true
)

lazy val greeting = taskKey[Unit]("An example task")
lazy val gitCommitSHA = taskKey[String]("Detemine the current git commit SHA")
lazy val printGit = taskKey[Unit]("Detemine the current git commit SHA")

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    greeting := {
      println(s"Hi, welcome to the ${name.value} project!")
    }
  )
  .settings(
    gitCommitSHA := Process("git rev-parse HEAD").lines.head
  )
  .settings(
    printGit := println(gitCommitSHA.value)
  )
  .settings(
    libraryDependencies ++= {
      val akkaVersion = "2.4.9-RC2"
      val sprayVersion = "1.3.3"
      val scalazVersion = "7.1.0"
      val slickVersion = "3.1.1"
      val hikariCPVersion = "2.4.6"
      val mysqlVersion = "5.1.39"
      val slfjVersion = "1.6.4"
      Seq(
        "com.typesafe.slick" %% "slick" % slickVersion,
        "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
        "mysql" % "mysql-connector-java" % mysqlVersion,
        "org.slf4j" % "slf4j-nop" % slfjVersion,
        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-agent" % akkaVersion,
        "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
        "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
        "com.typesafe.akka" %% "akka-remote" % akkaVersion,
        "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
        "com.typesafe.akka" %% "akka-stream" % akkaVersion,
        "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
        "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
        "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
        "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion,
        "com.typesafe.akka" %% "akka-http-jackson-experimental" % akkaVersion,
        "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
        "com.typesafe.akka" %% "akka-http-xml-experimental" % akkaVersion,
        "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
        "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
        "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
        "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
        "io.spray" %% "spray-can" % sprayVersion,
        "io.spray" %% "spray-routing" % sprayVersion,
        "io.spray" %% "spray-client" % sprayVersion,
        "io.spray" %% "spray-json" % "1.3.2",
        "org.scalaz" %% "scalaz-core" % scalazVersion,
        "org.scalaz" %% "scalaz-effect" % scalazVersion,
        "org.scalaz" %% "scalaz-typelevel" % scalazVersion,
        "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test",
        "org.scalatest" %% "scalatest" % "2.2.4",
        "org.scala-lang.plugins" %% "scala-continuations-library" % "1.0.2"
      )
    })
  .settings(
    addCompilerPlugin("org.scala-lang.plugins" % "scala-continuations-plugin_2.11.8" % "1.0.2")
  )
  .settings(
    scalacOptions += "-P:continuations:enable"
  )
  .settings(
    fork in run := true
  )









