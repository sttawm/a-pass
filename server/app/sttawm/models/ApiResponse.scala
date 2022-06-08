package sttawm.models

import play.api.libs.json.Json.toJson
import play.api.libs.json._
import play.api.mvc.Results

/**
  * A generic api response based on https://jsonapi.org
  */
case class ApiResponse[T](data: Option[T] = None, errors: Option[Seq[Error]] = None)

case class Error(title: String)

object ApiResponse {

  private implicit val errorFormat: OFormat[Error] = Json.format[Error]

  implicit def reads[T](implicit fmt: Reads[T]): Reads[ApiResponse[T]] = (json: JsValue) => {
    for {
      data <- (json \ "data").validateOpt[T]
      errors <- (json \ "errors").validateOpt[Seq[Error]]
    } yield ApiResponse(data, errors)
  }

  implicit def writes[T](implicit fmt: Writes[T]): Writes[ApiResponse[T]] =
    (o: ApiResponse[T]) =>
      JsObject(
        Seq(
          o.data.map("data" -> Json.toJson(_)),
          o.errors.map(arr => "errors" -> JsArray(arr.map(Json.toJson(_))))
        ).flatten
      )

  def ok[T](t: T)(implicit w: Writes[T]) = Results.Ok(toJson(ApiResponse(data = Some(t))))
}
