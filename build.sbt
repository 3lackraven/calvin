import sbt.Keys._

name := "Calvin"

version := "1.0"

scalaVersion := "2.12.3"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.4",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.4" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.4",
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.github.scopt" %% "scopt" % "3.7.0",
  "org.apache.tinkerpop" % "tinkergraph-gremlin" % "3.3.0" % "test"
)