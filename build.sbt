import sbt.Keys._

parallelExecution in ThisBuild := false

lazy val versions = new {
  val finatra = "2.1.5"
  val guice = "4.0"
  val logback = "1.0.13"
  val mockito = "1.9.5"
  val scalatest = "2.2.3"
  val specs2 = "2.3.12"
}

lazy val baseSettings = Seq(
  version := "1.0.0-SNAPSHOT",
  scalaVersion := "2.11.7",
  ivyScala := ivyScala.value.map(_.copy(overrideScalaVersion = true)),
  libraryDependencies ++= Seq(
    "org.mockito" % "mockito-core" % versions.mockito % "test",
    "org.scalatest" %% "scalatest" % versions.scalatest % "test",
    "org.specs2" %% "specs2" % versions.specs2 % "test"
  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    "Twitter Maven" at "https://maven.twttr.com"
  ),
  fork in run := true
)

lazy val root = (project in file(".")).
  settings(
    name := """finatra-thrift-seed""",
    organization := "com.example",
    moduleName := "activator-thrift-seed"
  ).
  aggregate(
    idl,
    client
  )

lazy val idl = (project in file("swiss-guard-idl")).
  settings(baseSettings).
  settings(
    name := "thrift-idl",
    moduleName := "thrift-idl",
    scroogeThriftDependencies in Compile := Seq(
      "finatra-thrift_2.11"
    ),
    libraryDependencies ++= Seq(
      "com.twitter.finatra" %% "finatra-thrift" % versions.finatra
    )
  )

lazy val client = (project in file("client"))
  .settings(baseSettings).
  settings(
    name := "thrift-client",
    moduleName := "thrift-client",
    libraryDependencies ++= Seq(
      "com.twitter" %% "finagle-core" % "6.34.0",
      "com.twitter" %% "finagle-stats" % "6.34.0",
      "com.twitter" % "finagle-thrift_2.11" % "6.34.0",
      "org.apache.thrift" % "libthrift" % "0.9.0" % "compile"
    )

  ).dependsOn(idl)

fork in run := true
