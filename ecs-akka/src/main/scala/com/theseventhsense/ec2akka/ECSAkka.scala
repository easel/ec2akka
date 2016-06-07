package com.theseventhsense.ec2akka

import akka.actor.{ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import akka.cluster.Cluster
import com.amazonaws.auth._
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ecs.AmazonECSClient
import com.typesafe.config.ConfigFactory

import scala.util.Try

/**
  * Created by erik on 5/19/16.
  */
object ECSAkka extends ExtensionId[ECSAkka] with ExtensionIdProvider {

  override def get(system: ActorSystem): ECSAkka = super.get(system)

  override def createExtension(system: ExtendedActorSystem): ECSAkka =
    new ECSAkka(system)

  override def lookup() = ECSAkka
}

class ECSAkka(system: ExtendedActorSystem) extends Extension {
  val settings = new EC2AkkaSettings(system)
  val selfAddress = Cluster(system).selfAddress
  val address = selfAddress
  //  val address = if (settings.host.nonEmpty && settings.port.nonEmpty) {
  //    system.log.info(s"host:port read from environment variables=${settings.host}:${settings.port}")
  //    selfAddress.copy(host = settings.host, port = settings.port)
  //  } else
  //    Cluster(system).selfAddress
  val myId = address.hostPort

  val ecsClient = Try {
    val credsProviderChain = new Ec2AkkaCredentialsProviderChain()
    val creds = credsProviderChain.getCredentials
    val client = new AmazonEC2Client(creds)
    val ecsClient = new AmazonECSClient()
    true
  }.getOrElse(true)

  private def tryJoin(): Boolean = {
    //    val myId = "self"
    //    val isLeader = true
    //    if (isLeader) {
    system.log.warning(
        "component=aws-ecs at=this-node-is-leader-node id={}", myId)
    Cluster(system).join(address)
    true
    //    } else {
    //      val seeds = latch.getParticipants.iterator().asScala.filterNot(_.getId == myId).map {
    //        node => AddressFromURIString(s"akka.tcp://${node.getId}")
    //      }.toList
    //      system.log.warning("component=zookeeper-cluster-seed at=join-cluster seeds={}", seeds)
    //      Cluster(system).joinSeedNodes(immutable.Seq(seeds: _*))
    //
    //      val joined = Promise[Boolean]()
    //
    //      Cluster(system).registerOnMemberUp {
    //        joined.trySuccess(true)
    //      }
    //
    //      try {
    //        Await.result(joined.future, 10.seconds)
    //      } catch {
    //        case _: TimeoutException => false
    //      }
    //    }
  }

  tryJoin()
}

class AkkaAWSECSSettings(system: ActorSystem) {

  private val config =
    Try(system.settings.config.getConfig("akka.cluster.aws-ecs"))
      .getOrElse(ConfigFactory.empty)

  val tag = Try(config.getString("tag")).getOrElse("akka")
}


