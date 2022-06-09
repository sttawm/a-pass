package sttawm.slick

import com.github.tminglei.slickpg._
import play.api.libs.json.{JsValue, Json}
import slick.basic.Capability
import slick.jdbc.JdbcCapabilities

/**
  * A custom 'slick profile' for mapping to custom types
  *
  * Also see build.sbt for the interplay for the actual creation of the fields with custom types.
  *
  * This file is adapted from: https://github.com/tminglei/slick-pg#usage
  */
trait SlickProfile extends slick.jdbc.PostgresProfile with PgArraySupport with PgDate2Support with PgPlayJsonSupport {
  def pgjson = "jsonb" // jsonb support is in postgres 9.4.0 onward; for 9.3.x use "json"

  // Add back `capabilities.insertOrUpdate` to enable native `upsert` support; for postgres 9.5+
  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + JdbcCapabilities.insertOrUpdate

  override val api = MyAPI

  // TODO: What is this for??
  // val plainApi = new API with DateTimeImplicits // with JodaDateTimeImplicits

  object MyAPI extends API with ArrayImplicits with DateTimeImplicits with JsonImplicits {
    implicit val strListTypeMapper = new SimpleArrayJdbcType[String]("text").to(_.toList)
    implicit val playJsonArrayTypeMapper =
      new AdvancedArrayJdbcType[JsValue](
        pgjson,
        s => utils.SimpleArrayUtils.fromString[JsValue](Json.parse)(s).orNull,
        v => utils.SimpleArrayUtils.mkString[JsValue](_.toString())(v)
      ).to(_.toList)
  }
}

object SlickProfile extends SlickProfile
