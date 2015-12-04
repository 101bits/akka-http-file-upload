import java.io.File
import java.lang.Long

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import akka.stream.javadsl.Sink
import spray.json._

import scala.concurrent.Future
import scala.io.StdIn.readLine

case class FileUploadResult(path: String, size: Int)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val fileUploadFormat = jsonFormat2(FileUploadResult)
}

object FileUploadServer extends Directives with JsonSupport {
  implicit val system = ActorSystem("fileUploadServer")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher


  val route =
    path("upload") {
      post {
        extractRequest { request =>
          println(request)
          val file = File.createTempFile("debug-", ".zip")
          val futureDone: Future[Long] = request.entity.dataBytes.runWith(Sink.file(file))

          onComplete(futureDone) { result =>
            complete(FileUploadResult(file.getAbsolutePath, file.length().toInt))
          }
        }
      }
    }

  def main(args: Array[String]) {
    val serverFuture: Future[ServerBinding] = Http().bindAndHandle(route, "localhost", 8080)

    println("FileUpload server is running at http://localhost:8080\nPress RETURN to stop ...")
    readLine()

    serverFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
