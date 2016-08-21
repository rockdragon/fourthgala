import Dependencies._
import sbt.complete.{Parser, DefaultParsers}
import DefaultParsers._

val crawlMoye = taskKey[Unit]("create the dependent-jars directory")
crawlMoye := {
  Process("rm -rf ./moye.me.html")
  url("http://www.moye.me/") #> file("./moye.me.html") !
}
val printMoye = taskKey[Unit]("print moye.me content")
printMoye := {
  val content = sbt.IO.read(file("./moye.me.html"))
  println(content) // scalastyle:ignore
}
val moyeCommand = Command.command("moye") {
  state => "crawlMoye" :: "printMoye" :: state
}
commands += moyeCommand

val source = taskKey[String]("source folder path")
val query = inputKey[Unit]("Runs a query")
val queryParser: Parser[String] = {
  token(any.* map (_.mkString))
}

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
      source := {
        val path = s"${sourceDirectory.value}"
        val log = streams.value.log
        log.log(Level.Warn, path)
        path
      }
    )
    .settings(
      query := {
        val parsed = queryParser.parsed
        val log = streams.value.log
        log.log(Level.Warn, parsed)
      }
    )
}

lazy val holyAkka = commonProject("holyAkka")
  .settings(
    libraryDependencies ++= akkaDependencies
  )
  .settings(
    version := "0.1.0"
  )

lazy val holyDB = commonProject("holyDB")
  .settings(
    libraryDependencies ++= dbDependencies
  )
  .settings(
    version := "0.2.0"
  )







