package com.theseventhsense.ec2akka

import com.typesafe.config.{Config, ConfigFactory, ConfigMergeable}

import scala.collection.JavaConverters._

object EC2AkkaConfigFactory {
  def load: Config=
    ConfigFactory.parseMap(
      Map(
        "akka.remote.netty.tcp.hostname" -> EC2Akka.privateIp
      ).asJava)
}
