package moc.sttawm.it

import sttawm.client.api.PingApi
import sttawm.client.model.{PingResponse, PingResponseApiResponse}
import org.scalatest.DoNotDiscover

@DoNotDiscover
object PingApiSpec extends AbstractIntegrationSpec {

  "The server" must {
    "be pingable" in {
      whenReady(invoker.execute(PingApi(baseUrl).ping())) { rsp =>
        rsp.code mustBe 200
        rsp.content mustBe PingResponseApiResponse(Some(PingResponse(msg = "I pong, therefore I am")))
      }
    }
  }
}
