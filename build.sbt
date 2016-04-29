name := """JEntity"""

version := "1.0"

lazy val JEntity = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  cache,
  javaJdbc,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "org.flywaydb" %% "flyway-play" % "2.2.1",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.6.0",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.6.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.0",
  "commons-lang" % "commons-lang" % "2.2",
  "org.apache.httpcomponents" % "httpasyncclient" % "4.1",
  "org.apache.commons" % "commons-email" % "1.3.1",
  "org.json" % "json" % "20141113",
  "org.lightcouch" % "lightcouch" % "0.1.6",
  "org.eclipse.jetty.npn" % "npn-api" % "8.1.2.v20120308",
  "org.eclipse.jetty.alpn" % "alpn-api" % "1.0.0",
  "com.ocpsoft" % "ocpsoft-pretty-time" % "1.0.7"
)

libraryDependencies += evolutions

enablePlugins(JavaServerAppPackaging)

inConfig(Test)(PlayEbean.scopedSettings)

playEbeanModels in Test := Seq("models.*")

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
