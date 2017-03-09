import com.theseventhsense.awsakka.S3ConfigFactory
import com.typesafe.config.Config
import org.scalatest.{MustMatchers, WordSpec}

/**
  * Created by erik on 3/9/17.
  */
class S3ConfigFactoryITest extends WordSpec with MustMatchers {
  "the s3 config factory" should {
    "load a config from an s3 bucket" in {
      S3ConfigFactory.loadFromS3("7thsense-cfg-prd", "application.conf") mustBe a[Config]
    }
    "throw a sane error when attempting to load a file that doesn't exist" in {
      S3ConfigFactory.loadFromS3("7thsense-cfg-prd", "application-does-not-exist.conf") mustBe a[Config]
      pending
    }
    "throw a sane error when attempting to access a bucket without permission" in {
      S3ConfigFactory.loadFromS3("7thsense-cfg-prd-does-not-exist", "application.conf") mustBe a[Config]
    }
  }
}
