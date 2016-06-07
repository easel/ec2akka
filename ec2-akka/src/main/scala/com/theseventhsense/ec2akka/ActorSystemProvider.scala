package com.theseventhsense.ec2akka

import java.util.concurrent.TimeoutException
import javax.inject.{Inject, Provider, Singleton}

import akka.actor.ActorSystem
import com.typesafe.config.Config

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.{Duration, FiniteDuration}

/**
  * Created by erik on 5/20/16.
  */

object ActorSystemProvider {

  type StopHook = () => Future[_]

  private val logger = Logger(classOf[ActorSystemProvider])

  /**
    * Start an ActorSystem, using the given configuration and ClassLoader.
    * @return The ActorSystem and a function that can be used to stop it.
    */
  def start(classLoader: ClassLoader, configuration: Configuration): (ActorSystem, StopHook) = {
    val config = PlayConfig(configuration)

    val akkaConfig: Config = {
      val akkaConfigRoot = config.get[String]("play.akka.config")
      // Need to fallback to root config so we can lookup dispatchers defined outside the main namespace
      config.get[Config](akkaConfigRoot).withFallback(config.underlying)
    }

    val name = config.get[String]("play.akka.actor-system")
    val system = ActorSystem(name, akkaConfig, classLoader)
    logger.debug(s"Starting application default Akka system: $name")

    val stopHook = { () =>
      logger.debug(s"Shutdown application default Akka system: $name")
      system.terminate()

      config.get[Duration]("play.akka.shutdown-timeout") match {
        case timeout: FiniteDuration =>
          try {
            Await.result(system.whenTerminated, timeout)
          } catch {
            case te: TimeoutException =>
              // oh well.  We tried to be nice.
              logger.info(s"Could not shutdown the Akka system in $timeout milliseconds.  Giving up.")
          }
        case _ =>
          // wait until it is shutdown
          Await.result(system.whenTerminated, Duration.Inf)
      }

      Future.successful(())
    }

    (system, stopHook)
  }

  /**
    * A lazy wrapper around `start`. Useful when the `ActorSystem` may
    * not be needed.
    */
  def lazyStart(classLoader: => ClassLoader, configuration: => Configuration): ClosableLazy[ActorSystem, Future[_]] = {
    new ClosableLazy[ActorSystem, Future[_]] {
      protected def create() = start(classLoader, configuration)
      protected def closeNotNeeded = Future.successful(())
    }
  }

}
