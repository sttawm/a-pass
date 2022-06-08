package sttawm.controllers

import play.api.mvc.{AbstractController, ControllerComponents}
import sttawm.models.ApiResponse.ok
import sttawm.models.PingResponse
import sttawm.models.PingResponse.format

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class PingController @Inject() (cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def ping() =
    Action {
      ok(PingResponse())
    }
}
