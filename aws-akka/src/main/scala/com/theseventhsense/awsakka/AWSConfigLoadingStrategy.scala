package com.theseventhsense.awsakka

import com.typesafe.config.{Config, ConfigParseOptions, DefaultConfigLoadingStrategy}

class AWSConfigLoadingStrategy extends DefaultConfigLoadingStrategy {
  override def parseApplicationConfig(parseOptions: ConfigParseOptions): Config = {
    sys.props.get("config.url") match {
      case Some(url) if url.startsWith("s3:") =>
        S3ConfigFactory.loadFromS3Url(url)
      case _ =>
        super.parseApplicationConfig(parseOptions)
    }
  }
}
