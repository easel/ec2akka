organization in ThisBuild := "com.theseventhsense.ec2akka"

version in ThisBuild := "0.0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

isSnapshot in ThisBuild := true

val akkaVersion = "2.4.6"
val awsSdkVersion = "1.10.77"

def akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion % "provided",
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion % "provided",
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion % "provided",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.7" % "provided",
  "ch.qos.logback" % "logback-classic" % "1.1.2" % "provided"
)

def awsDependencies = Seq(
  "com.amazonaws" % "aws-java-sdk" % awsSdkVersion % "provided"
)

libraryDependencies in ThisBuild ++= akkaDependencies ++ awsDependencies

val root = akkaEc2EcsProject

lazy val akkaEc2EcsProject = Project("ec2akka", file("."))
  .aggregate(exampleProject)
  .aggregate(akkaEcsProject)
  .aggregate(akkaEc2Project)

lazy val exampleProject = Project("example", file("./example"))
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-http-core" % akkaVersion)
  .enablePlugins(JavaAppPackaging)
  .dependsOn(akkaEcsProject)

lazy val akkaEc2Project = Project("ec2", file("./ec2-akka"))
  .settings(Defaults.coreDefaultSettings: _*)
  .settings(
    scalacOptions in Compile ++= Seq("-encoding", "UTF-8", "-target:jvm-1.6", "-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint")
  )

lazy val akkaEcsProject = Project("ecs", file("./ecs-akka"))
  .dependsOn(akkaEc2Project)
  .settings(Defaults.coreDefaultSettings: _*)
  .settings(
    scalacOptions in Compile ++= Seq("-encoding", "UTF-8", "-target:jvm-1.6", "-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint")
  )

