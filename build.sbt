import sbt.Keys._
import Dependencies._

val moye = taskKey[Unit]("create the dependent-jars directory")
moye := {
  url("http://www.moye.me/") #> file("./moye.me.html") !
}
val source = taskKey[String]("source folder path")

def commonProject(name: String): Project = {
  Project(name, file(name))
    .settings(Seq(
      scalaVersion := "2.11.8",
      autoCompilerPlugins := true
    ): _*)
    .settings(
      addCompilerPlugin("org.scala-lang.plugins" % "scala-continuations-plugin_2.11.8" % scalaPluginVersion)
    )
    .settings(
      scalacOptions += "-P:continuations:enable"
    )
    .settings(
      fork in run := true
    )
    .settings(
      source := s"${sourceDirectory.value}"
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










