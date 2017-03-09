package com.theseventhsense.ec2akka

import com.typesafe.config.Config

/**
  * Created by erik on 3/9/17.
  */
object S3ConfigFactory {
  lazy val client = AmazonS3ClientBuilder.defaultClient
  def loadFromS3(url: String): Config = {
    
    
  }

}
