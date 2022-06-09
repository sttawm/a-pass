package moc.sttawm.it

import org.scalatest.{Suites, TestSuite}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite

class AppSpec extends Suites(PingApiSpec) with TestSuite with GuiceOneServerPerSuite {}
