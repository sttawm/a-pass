package sttawm.models

import play.api.libs.json.Json

case class PingResponse(msg: String = "I pong, therefore I am")

object PingResponse {

  implicit val format = Json.format[PingResponse]
}
