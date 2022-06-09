/*
  This is an Ammonite Script
  http://ammonite.io/#Ammonite

  Usage: amm ./slick-codegen.sc
*/

/*
   This script is copied and modified from: https://lombardo-chcg.github.io/tools/2018/08/09/slick-codegen-on-postgres-views.html

	 There are a few August-Health modifications in this file:
	 1. Pull the case-classes out (for play-json macros)
	 2. Replace JdbcProfile with AugustSlickProfile in the trait
	 3. Pull the custom imports out (because we pulled out the case-classes)
	 4. Usage of custom types (joda-time and play-json)
	 5. Ignore some columns
 */

import scala.language.postfixOps
import $ivy.{`ch.qos.logback:logback-classic:1.2.3`, `com.lihaoyi:sourcecode_2.13:0.2.8`, `com.typesafe.slick:slick-codegen_2.13:3.3.3`, `com.typesafe.slick:slick_2.13:3.3.3`, `io.github.cdimascio:java-dotenv:5.2.2`, `org.postgresql:postgresql:42.2.2`}
import slick.codegen.SourceCodeGenerator

import scala.collection.mutable.ListBuffer
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.meta._
import slick.model.Model
import slick.sql.SqlProfile.ColumnOption.SqlType

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.github.cdimascio.dotenv.Dotenv

import java.nio.file.{Paths}

LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger].setLevel(Level.INFO)

// TODO: Read from .env
val projectHome = Paths.get(sourcecode.File()).getParent.getParent.toString
System.err.println("Using .env " + projectHome)
val dotenv = Dotenv.configure().directory(projectHome).load()

/**
 * Configuration
 */
val port = 5432
val user = dotenv.get("DB_USERNAME")
val password = dotenv.get("DB_PASSWORD")
val database = dotenv.get("DB_DATABASE")
val folder = Paths.get(projectHome, "server", "slick", "src", "main", "scala").toString

val	url = s"jdbc:postgresql://localhost:${port}/${database}"
val profile = "SlickProfile"

val db = Database.forURL(url, user, password)

/**
 * A customized code generator in order to support play-json macros
 * @see https://github.com/slick/slick/issues/1382
 */
class CustomizedCodeGenerator(model: Model) extends SourceCodeGenerator(model) {
	val models = new ListBuffer[String]

	/**
	 * Generates code providing the data model as trait and object in a Scala package
	 * @group Basic customization overrides
	 * @param profile Slick profile that is imported in the generated package (e.g. slick.jdbc.H2Profile)
	 * @param pkg Scala package the generated code is placed in
	 * @param container The name of a trait and an object the generated code will be placed in within the specified package.
	 */
	override def packageCode(profile: String, pkg: String, container: String, parentType: Option[String]): String = {

		s"""
		package ${pkg}

       ${customImports}

		// AUTO-GENERATED Slick data model
		/** Stand-alone Slick data model for immediate use */
		object ${container} extends {
			val profile = $profile
		} with ${container}

		/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
		trait ${container}${parentType.map(t => s" extends $t").getOrElse("")} {
			val profile: $profile
			import profile.api._
			${indent(code)}
		}
		$outsideCode
			""".trim()
	}

	def customImports =
	"""
    | import play.api.libs.json.JsValue
    | import java.time.{LocalDate, LocalTime, LocalDateTime, Duration, ZonedDateTime, OffsetDateTime}
	|""".stripMargin

	def outsideCode = s"${models.mkString("\n")}"

	/**
	 * Moves the Row(s) outside of the auto-generated 'trait Tables'
	 */
	override def Table = new Table(_) {

			override def EntityType = new EntityTypeDef {
				override def docWithCode: String = {
					models += super.docWithCode + "\n"
					""
				}
			}

			override def Column = new Column(_) {
				override def rawType = {
					val sqlType: Option[String] = model.options.flatMap {
						case SqlType(t) => Some(t)
						case _ => None // None
					}.headOption

					sqlType match {
						// Play-Json Mapping
						case Some("jsonb") => "JsValue"

						// See https://github.com/tminglei/slick-pg
						case Some("timestamptz") => "DateTime"
						case Some("timestamp") => "ZonedDateTime"
						case Some("date") => "LocalDate"

						// Arrays
						case Some("_text") => "List[String]"

						case _ => super.rawType
					}
				}
			}
		}


	override def code = // Import custom types
	  "import play.api.libs.json.JsValue\n" +
      "import java.time.{LocalDate, LocalTime, LocalDateTime, Duration, ZonedDateTime, OffsetDateTime}\n" +
	  super.code
}

/**
 * Create tables and views (view-creation is not possible using the standard sbt plugin)
 */
val tablesAndViews = MTable.getTables(None, None, None, Some(Seq("TABLE", "VIEW")))
val modelAction = PostgresProfile.createModel(Some(tablesAndViews))
val model = Await.result(db.run(modelAction), 120 seconds)
val codeGen = new CustomizedCodeGenerator(model)

codeGen.writeToFile(
	profile = profile,
	folder = folder,
	pkg = "sttawm.slick",
	container = "Tables",
	fileName ="Tables.scala"
)
