package com.theseventhsense.ec2akka

import com.typesafe.config.{Config, ConfigFactory}
import scala.collection.JavaConverters._

class EC2AkkaConfigFactory {
  def load(
      configuration: Config
  ): Config =
    ConfigFactory.parseMap(
      Map(
        "akka.remote.netty.tcp.hostname" -> EC2Akka.privateIp
      ).asJava)
}
