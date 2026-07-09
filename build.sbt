name := """leave-accrual-ledger"""
organization := "com.dokuqui"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.18"
val doobieVersion = "1.0.0-RC1"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  "org.playframework" %% "play-jdbc-evolutions" % "3.0.11",
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari" % doobieVersion,
  "com.mysql" % "mysql-connector-j" % "8.2.0",
)
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.dokuqui.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.dokuqui.binders._"
