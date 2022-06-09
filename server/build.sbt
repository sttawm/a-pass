ThisBuild / organization := "sttawm"
ThisBuild / version := "0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"
Global / onChangedBuildSource := ReloadOnSourceChanges

val playVersion = "2.8.16"
val playJsonVersion = "2.9.2"
val jacksonVersion = "2.13.3"
val playSlickVersion = "5.0.2"
val testcontainersVersion = "0.40.8"

lazy val root = (project in file("."))
  .aggregate(slick)
  .dependsOn(slick)
  .enablePlugins(PlayScala)
  .settings(
    name := "server",
    libraryDependencies ++= Seq(
      guice,
      // slick
      "com.typesafe.play" %% "play-slick" % playSlickVersion,
      "com.typesafe.play" %% "play-slick-evolutions" % playSlickVersion,
      // json
      "com.typesafe.play" %% "play-json" % playJsonVersion,
      //logging
      "net.logstash.logback" % "logstash-logback-encoder" % "7.2",
      "com.fasterxml.jackson.module" % "jackson-module-scala_2.13" % jacksonVersion,
      "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
      "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
      // tests
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
      "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersVersion % Test,
      "com.dimafeng" %% "testcontainers-scala-postgresql" % testcontainersVersion % Test
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
    javacOptions ++= List("-Xlint:unchecked", "-Xlint:deprecation", "-Werror"),
    ThisBuild / envFileName := "../.env",
    Test / javaOptions += "-Dlogger.resource=logback.test.xml"
  )

lazy val slick = project
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.3.3",
      "org.postgresql" % "postgresql" % "42.3.6",
      "com.typesafe.play" %% "play-json" % playJsonVersion,
      "com.github.tminglei" %% "slick-pg" % "0.20.3",
      "com.github.tminglei" %% "slick-pg_play-json" % "0.20.3"
    )
  )
