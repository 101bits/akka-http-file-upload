name := "akka-http-file-upload"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

import sbt._

lazy val root = (project in file("."))
  .settings(libraryDependencies ++= Seq(
    "com.typesafe.akka" % "akka-actor_2.11" % "2.4-SNAPSHOT",
    "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.0-M2",
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.0-M2",
    "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
    "com.typesafe.akka" %% "akka-http-testkit-experimental" % "2.0-M2"
  ))