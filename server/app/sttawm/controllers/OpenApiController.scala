package sttawm.controllers

import com.iheart.playSwagger.{
  OutputTransformer,
  ParametricType,
  ParametricTypeNamesTransformer,
  PrefixDomainModelQualifier,
  SwaggerSpecGenerator
}
import play.api.Configuration
import play.api.libs.json.{JsArray, JsObject, JsString}
import play.api.mvc._

import javax.inject.Inject
import scala.util.{Success, Try}

class OpenApiController @Inject() (cc: ControllerComponents, config: Configuration) extends AbstractController(cc) {

  private implicit val cl = getClass.getClassLoader

  private lazy val generator =
    SwaggerSpecGenerator(
      swaggerV3 = true,
      modelQualifier = PrefixDomainModelQualifier("sttawm.models"),
      outputTransformers = Seq(ApiResponseNamesTransformer)
    )

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

private object ApiResponseNamesTransformer extends OutputTransformer {

  override def apply(obj: JsObject): Try[JsObject] = Success(tf(obj))

  private def tf(obj: JsObject): JsObject =
    JsObject {
      obj.fields.map {
        case (key, value: JsObject) => (normalize(key), tf(value))
        case (key, JsString(value)) => (normalize(key), JsString(normalize(value)))
        case (key, other)           => (normalize(key), other)
        case e                      => e
      }
    }

  private final val normalize: String => String = {
    case ParametricType.ParametricTypeClassName(className, argsGroup) =>
      val normalizedArgs =
        argsGroup
          .split(",")
          .iterator
          .map(_.trim)
          .map(normalize)
          .mkString("_")
      s"$className-$normalizedArgs${if (className.endsWith(".ApiResponse")) "ApiResponse" else ""}"
    case n => n
  }
}
