package moc.sttawm.controllers

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents, Results}

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class PingController @Inject() (cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {
  def ping() = Action { Results.Ok(Json.obj("I pong" -> "therefore I am")) }
}
