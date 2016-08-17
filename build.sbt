import sbt.Keys._
import Dependencies._

def commonProject(name:String): Project = {
  Project(name, file(name))
    .settings(Seq(
      scalaVersion := "2.11.8",
      autoCompilerPlugins := true
    ): _*)
    .settings(
      addCompilerPlugin("org.scala-lang.plugins" % "scala-continuations-plugin_2.11.8" % "1.0.2")
    )
    .settings(
      scalacOptions += "-P:continuations:enable"
    )
    .settings(
      fork in run := true
    )
}

lazy val holyAkka = commonProject("holyAkka")
    .settings(
      libraryDependencies ++= akkaDependencies
    )

lazy val holySlick = commonProject("holySlick")
    .settings(
      libraryDependencies ++= slickDependencies
    )










