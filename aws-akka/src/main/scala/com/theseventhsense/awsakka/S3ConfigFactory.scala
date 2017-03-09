package com.theseventhsense.awsakka

import java.io.InputStreamReader

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by erik on 3/9/17.
  */
object S3ConfigFactory {
  lazy val client = AmazonS3ClientBuilder.defaultClient
  def loadFromS3(bucketName: String, key: String): Config = {
    val s3Object = client.getObject(bucketName, key)
    ConfigFactory.parseReader(new InputStreamReader(s3Object.getObjectContent))
  }

}
