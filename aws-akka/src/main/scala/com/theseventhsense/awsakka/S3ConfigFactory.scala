package com.theseventhsense.awsakka

import java.io.InputStreamReader

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.typesafe.config.Config

/**
  * Created by erik on 3/9/17.
  */
object S3 {
  private lazy val client = AmazonS3ClientBuilder.defaultClient
  //private val S3UrlRegex = """/s3:\/\/(\\w+)\/(.*)/""".r
  private val S3UrlRegex = """s3://([-\w]+)/(.*)""".r
  object ConfigFactory {
    def parseS3Url(urlString: String): Option[(String, String)] = {
      S3UrlRegex.findFirstMatchIn(urlString).map { m =>
        (m.group(1), m.group(2))
      }
    }
    
    def loadFromS3Url(url: String): Config = 
      (loadFromS3 _).tupled(parseS3Url(url).getOrElse(throw new RuntimeException(s"Invalid s3 url: $url")))
    
    
    def loadFromS3(bucketName: String, key: String): Config = {
      val s3Object = client.getObject(bucketName, key)
      com.typesafe.config.ConfigFactory.parseReader(
        new InputStreamReader(s3Object.getObjectContent))
    }
  }

}
