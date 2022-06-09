package moc.sttawm.it

import akka.actor.ActorSystem
import sttawm.client.core.ApiInvoker
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatestplus.play.{ConfiguredServer, PlaySpec}
import play.api.Logging

abstract class AbstractIntegrationSpec extends PlaySpec with ConfiguredServer with ScalaFutures with Logging {

  private implicit val system = ActorSystem()

  val invoker = ApiInvoker()

  /* The amount to wait for futures when using `whenReady() { ... }` */
  override implicit def patienceConfig: PatienceConfig =
    super.patienceConfig.copy(timeout = Span(3, Seconds))

  def baseUrl = s"http://localhost:$port"
}
