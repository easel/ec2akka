organization in ThisBuild := "com.theseventhsense.ec2akka"
bintrayOrganization in ThisBuild := Some("7thsense")
licenses in ThisBuild += ("MIT", url("http://opensource.org/licenses/MIT"))
version in ThisBuild := "0.0.9-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.5"
crossScalaVersions in ThisBuild := Seq("2.11.11", "2.12.5")
isSnapshot in ThisBuild := version.value.contains("SNAPSHOT")
publishMavenStyle in ThisBuild := true

val compileFlags = Seq("-encoding",
                       "UTF-8",
                       "-target:jvm-1.6",
                       "-deprecation",
                       "-feature",
                       "-unchecked",
                       "-Xlog-reflective-calls",
                       "-Xlint")

libraryDependencies in ThisBuild ++= Dependencies.Akka

val root = akkaEc2EcsProject

lazy val akkaEc2EcsProject = Project("ec2akka", file("."))
  .settings(publish := {})
  .aggregate(exampleProject)
  .aggregate(akkaAwsProject)
  .aggregate(akkaEcsProject)
  .aggregate(akkaEc2Project)

lazy val exampleProject = Project("example", file("./example"))
  .settings(publish := {})
  .settings(libraryDependencies ++= Dependencies.AkkaHttp)
  .enablePlugins(JavaAppPackaging)
  .dependsOn(akkaEcsProject)

lazy val akkaAwsProject = Project("aws-akka", file("./aws-akka"))
  .settings(Defaults.coreDefaultSettings: _*)
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(libraryDependencies ++= Dependencies.AwsSdkS3)
  .settings(libraryDependencies ++= Dependencies.ScalaTest)

lazy val akkaEc2Project = Project("ec2-akka", file("./ec2-akka"))
  .dependsOn(akkaAwsProject)
  .settings(Defaults.coreDefaultSettings: _*)
  .settings(
    scalacOptions in Compile ++= compileFlags
  )
  .settings(libraryDependencies ++= Dependencies.AwsSdkEC2)

lazy val akkaEcsProject = Project("ecs-akka", file("./ecs-akka"))
  .dependsOn(akkaEc2Project)
  .settings(Defaults.coreDefaultSettings: _*)
  .settings(
    scalacOptions in Compile ++= compileFlags
  )
  .settings(libraryDependencies ++= Dependencies.AwsSdkEC2)
  .settings(libraryDependencies ++= Dependencies.AwsSdkECS)
