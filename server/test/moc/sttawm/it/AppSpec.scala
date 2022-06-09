package moc.sttawm.it

import com.dimafeng.testcontainers.PostgreSQLContainer
import org.scalatest.{BeforeAndAfterAll, Suites, TestSuite}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import org.testcontainers.containers.PostgreSQLContainer.POSTGRESQL_PORT
import org.testcontainers.utility.DockerImageName
import play.api.Logging
import play.api.inject.guice.GuiceApplicationBuilder

class AppSpec extends Suites with TestSuite with GuiceOneServerPerSuite with Logging with BeforeAndAfterAll {

  lazy val container =
    PostgreSQLContainer(dockerImageNameOverride = DockerImageName.parse("postgres:13-alpine"))

  override def fakeApplication() = {
    var applicationBuilder = new GuiceApplicationBuilder()

    if (System.getProperty("useRemotePostgres", "false").toBoolean) {
      logger.info("Testing against remote postgres")

    } else {
      logger.info(s"Testing against embedded postgres")
      container.start()
      logger.info(s"Started postgres at ${container.jdbcUrl}")
      applicationBuilder = applicationBuilder.configure(
        "slick.dbs.default.host" -> container.host,
        "slick.dbs.default.port" -> container.mappedPort(POSTGRESQL_PORT),
        "slick.dbs.default.database" -> container.databaseName,
        "slick.dbs.default.username" -> container.username,
        "slick.dbs.default.password" -> container.password
      )
    }

    applicationBuilder.build()
  }

  override protected def afterAll(): Unit = container.stop()

}
