package com.theseventhsense.ec2akka

import scala.collection.JavaConverters._
import scala.util.Try

import akka.actor.{ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import akka.cluster.Cluster
import com.amazonaws.services.ecs.AmazonECSClientBuilder
import com.amazonaws.services.ecs.model.DescribeContainerInstancesRequest
import com.typesafe.config.ConfigFactory

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
  val cluster = Cluster(system)
  val selfAddress = cluster.selfAddress
  val address = selfAddress
  //  val address = if (settings.host.nonEmpty && settings.port.nonEmpty) {
  //    system.log.info(s"host:port read from environment variables=${settings.host}:${settings.port}")
  //    selfAddress.copy(host = settings.host, port = settings.port)
  //  } else
  //    Cluster(system).selfAddress
  val myId = address.hostPort

  val ecsClient = Try {
    val credsProviderChain = new Ec2AkkaCredentialsProviderChain()
//    val creds = credsProviderChain.getCredentials
//    val client = AmazonEC2ClientBuilder.standard().withCredentials(credsProviderChain).build()
    val ecsClient = AmazonECSClientBuilder
      .standard()
      .withCredentials(credsProviderChain)
      .build()
    ecsClient
  }.get

  def downUnavailableNodes(): Unit = {
    cluster.state.unreachable
    val describeRequest = new DescribeContainerInstancesRequest()
    describeRequest.setCluster("seed")
    val response = ecsClient.describeContainerInstances(describeRequest)
    val instances = response.getContainerInstances.asScala
    instances.foreach { instance =>
      ()
    }
  }

  private def tryJoin(): Boolean = {
    //    val myId = "self"
    //    val isLeader = true
    //    if (isLeader) {
    system.log
      .warning("component=aws-ecs at=this-node-is-leader-node id={}", myId)
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
