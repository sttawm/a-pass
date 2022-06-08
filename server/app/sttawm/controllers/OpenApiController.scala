package sttawm.controllers

import com.iheart.playSwagger.SwaggerSpecGenerator
import play.api.Configuration
import play.api.libs.json.{JsArray, JsObject, JsString}
import play.api.mvc._

import javax.inject.Inject

class OpenApiController @Inject() (cc: ControllerComponents, config: Configuration) extends AbstractController(cc) {

  private implicit val cl = getClass.getClassLoader

  private lazy val generator = SwaggerSpecGenerator(true, "sttawm.models")

  private val host = config.get[String]("openapi.host")

  private lazy val openapi = Action { _ =>
    generator
      .generate()
      .map(_ + ("servers" -> JsArray(List(JsObject(Seq("url" -> JsString(host)))))))
      .fold(
        e => {
          e.printStackTrace()
          InternalServerError("Couldn't generate swagger.json")
        },
        s => Ok(s)
      )
  }

  def specs = openapi

  def redirectDocs =
    Action {
      Redirect(
        url = "/docs/swagger-ui/index.html",
        queryStringParams = Map("docExpansion" -> Seq("none"), "url" -> Seq("/assets/swagger.json"))
      )
    }
}
