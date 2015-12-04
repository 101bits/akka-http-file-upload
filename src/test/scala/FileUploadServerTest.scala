import FileUploadServer.route
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest.{FlatSpec, ShouldMatchers}

class FileUploadServerTest extends FlatSpec
with ShouldMatchers
with ScalatestRouteTest
with JsonSupport {
  behavior of "file upload server"

  "it" should "upload a text file successfully" in {
    val fileContent: String = "Hello World!"
    val entity = HttpEntity.Strict(MediaTypes.`application/octet-stream`, ByteString(fileContent + "\n"))

    Post("/upload", entity) ~> route ~> check {
      status shouldBe StatusCodes.OK

      val response = responseAs[FileUploadResult]
      response.size should be > 0

      val content = scala.io.Source.fromFile(response.path).getLines().toArray
      content should have size 1
      content.head shouldBe fileContent
    }
  }
}