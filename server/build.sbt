ThisBuild / organization := "moc.sttawm"
ThisBuild / version := "0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"
Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "play-server",
    // swagger
    resolvers += Resolver.jcenterRepo,
    libraryDependencies ++= Seq(
      guice,
      // json
      "com.typesafe.play" %% "play-json" % "2.9.2",
      // swagger
      "com.iheart" %% "play-swagger" % "0.10.6-PLAY2.8",
      "org.webjars" % "swagger-ui" % "3.43.0",
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
