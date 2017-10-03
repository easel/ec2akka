package com.theseventhsense.awsakka

import com.typesafe.config.{Config, ConfigParseOptions, DefaultConfigLoadingStrategy}
import org.slf4j._

class AWSConfigLoadingStrategy extends DefaultConfigLoadingStrategy {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  logger.debug("Loaded AWSConfigLoadingStrategy")

  override def parseApplicationConfig(parseOptions: ConfigParseOptions): Config = {
    sys.props.get("config.url") match {
      case Some(url) if url.startsWith("s3:") =>
        logger.info(s"Loading root configuration from $url")
        S3ConfigFactory.loadFromS3Url(url)
      case _ =>
        logger.info(s"No config.url or it does not begin with s3, falling back to DefaultConfigLoadingStrategy")
        super.parseApplicationConfig(parseOptions)
    }
  }
}
