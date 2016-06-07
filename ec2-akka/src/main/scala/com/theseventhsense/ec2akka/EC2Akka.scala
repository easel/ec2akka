package com.theseventhsense.ec2akka

import akka.actor.{ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.util.EC2MetadataUtils
import com.typesafe.config.ConfigFactory

import scala.util.Try

object EC2Akka extends ExtensionId[EC2Akka] with ExtensionIdProvider {

  override def get(system: ActorSystem): EC2Akka = super.get(system)

  override def createExtension(system: ExtendedActorSystem): EC2Akka =
    new EC2Akka(system)

  override def lookup() = EC2Akka

  private val providerChain = new Ec2AkkaCredentialsProviderChain()

  def creds: AWSCredentials = providerChain.getCredentials

  def client = new AmazonEC2Client(creds)

  lazy val instanceId = EC2MetadataUtils.getInstanceId

  lazy val privateIp = EC2MetadataUtils.getPrivateIpAddress

  lazy val availabilityZone = EC2MetadataUtils.getAvailabilityZone

  lazy val region = EC2MetadataUtils.getEC2InstanceRegion

}

class EC2Akka(system: ExtendedActorSystem) extends Extension {
  val config = new EC2AkkaSettings(system)


}

class EC2AkkaSettings(system: ActorSystem) {

  private val config =
    Try(system.settings.config.getConfig("akka.cluster.aws.ec2"))
      .getOrElse(ConfigFactory.empty)

}
