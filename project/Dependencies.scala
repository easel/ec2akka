import sbt._

object Dependencies {
  object Versions {
    val Akka = "2.5.23"
    val AkkaHttp = "10.1.8"
    val AwsSdk = "1.11.563"
    val ScalaLogging = "3.9.2"
    val ScalaTest = "3.0.7"
  }

  val ScalaTest = Seq(
    "org.scalatest" %% "scalatest" % Versions.ScalaTest % "it,test"
  )

  val Akka = Seq(
    "com.typesafe.akka" %% "akka-actor" % Versions.Akka % "provided",
    "com.typesafe.akka" %% "akka-cluster" % Versions.Akka % "provided",
    "com.typesafe.akka" %% "akka-slf4j" % Versions.Akka % "provided",
    "org.slf4j" % "log4j-over-slf4j" % "1.7.7" % "provided",
    "ch.qos.logback" % "logback-classic" % "1.1.2" % "provided"
  )

  val AkkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http-core" % Versions.AkkaHttp
  )

  val AwsSdk = Seq(
    "com.amazonaws" % "aws-java-sdk" % Versions.AwsSdk % "provided"
  )

  val AwsSdkS3 = Seq(
    "com.amazonaws" % "aws-java-sdk-s3" % Versions.AwsSdk % "provided"
  )

  val AwsSdkEC2 = Seq(
    "com.amazonaws" % "aws-java-sdk-ec2" % Versions.AwsSdk % "provided"
  )

  val AwsSdkECS = Seq(
    "com.amazonaws" % "aws-java-sdk-ecs" % Versions.AwsSdk % "provided"
  )

  val ScalaLogging = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % Versions.ScalaLogging
  )
}
