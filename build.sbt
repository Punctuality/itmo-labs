name := "ITMO-University-Java-Labor"
organization := "ru.ifmo.se"

scalaVersion in ThisBuild := "2.12.8"
version := "6.0"

// PROJECTS

lazy val global = project
  .in(file("."))
  .settings(settings)
  .aggregate(
    common,
    clientSide,
    serverSide
  )

lazy val common = project
  .in(file("common"))
  .settings(
    name := "common",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(
    serverSide
  )

lazy val clientSide = project
  .in(file("client"))
  .settings(
    name := "client",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(
    common,
    serverSide
  )

lazy val serverSide = project
  .in(file("server"))
  .settings(
    name := "server",
    settings,
    libraryDependencies ++= commonDependencies ++ List(
      "org.postgresql" % "postgresql" % "42.1.1",
      "com.typesafe.slick" %% "slick" % "3.3.0",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0"
    )
  )
  .dependsOn(
    common
  )

// DEPENDENCIES

lazy val dependencies =
  new {
    // Versions

    val json4sVersion = "3.6.5"
    val mailAPIVersion = "1.5.0-b01"
//    val scalaLoggingVersion = "3.9.2"
//    val akkaVersion = "2.5.21"
//    val akkaActorVersion = "2.5.21"
//    val akkaStreamVersion = "2.5.21"
//    val akkaHttpVersion = "10.1.7"
//
//    val scalaTestVersion = "3.0.5"

    // Main Dependencies

    val javamail = "javax.mail" % "mail" % mailAPIVersion
    val json4s = "org.json4s" %% "json4s-jackson" % json4sVersion
//    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
//    val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaActorVersion
//    val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaStreamVersion
//    val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion

    // Test Dependencies

//    val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % Test
//    val akkaStreamTest = "com.typesafe.akka" %% "akka-stream-testkit" % akkaStreamVersion % Test
//    val akkaHttpTest = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
//    val akkaTest = "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
  }

lazy val commonDependencies = Seq(
  dependencies.json4s,
  dependencies.javamail
//  dependencies.akkaActor,
//  dependencies.akkaStream,
//  dependencies.akkaHttp,
//  dependencies.scalaTest,
//  dependencies.scalaLogging,
//  dependencies.akkaStreamTest,
//  dependencies.akkaTest,
//  dependencies.akkaHttpTest
)

// SETTINGS

lazy val settings =
  commonSettings

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions
)