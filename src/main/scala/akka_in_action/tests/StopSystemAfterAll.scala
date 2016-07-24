package akka_in_action.tests

import akka.testkit.TestKit
import org.scalatest.{Suite, BeforeAndAfterAll}

trait StopSystemAfterAll extends BeforeAndAfterAll {
  this: TestKit with Suite =>
  override protected def afterAll: Unit = {
    super.afterAll

    system.shutdown
  }
}
