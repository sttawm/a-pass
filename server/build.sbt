ThisBuild / organization := "sttawm"
ThisBuild / version := "0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"
Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "play-server",
    libraryDependencies ++= Seq(
      guice,
      // json
      "com.typesafe.play" %% "play-json" % "2.9.2"
    ),
    scalacOptions ++= List(
      "-encoding",
      "utf8",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xfatal-warnings",
      "-language:postfixOps"
    ),
    javacOptions ++= List("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")
  )
