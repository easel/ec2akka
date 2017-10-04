package com.theseventhsense.ec2akka

import com.theseventhsense.awsakka.AWSConfigLoadingStrategy
import com.typesafe.config.{Config, ConfigParseOptions}

class EC2ConfigLoadingStrategy extends AWSConfigLoadingStrategy {
  override def parseApplicationConfig(parseOptions: ConfigParseOptions): Config =
    super.parseApplicationConfig(parseOptions).withFallback(EC2AkkaConfigFactory.load)
}
