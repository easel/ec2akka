import sbt._

object Dependencies {
  object Versions {
    val Akka = "2.4.17"
    val AkkaHttp = "10.0.4"
    val AwsSdk = "1.11.102"
    val ScalaTest = "3.0.1"
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

}
