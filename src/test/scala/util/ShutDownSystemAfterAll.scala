package util

import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Suite}

trait ShutDownSystemAfterAll extends BeforeAndAfterAll {
  this: TestKit with Suite =>
  override protected def afterAll(): Unit = {
    super.afterAll()
    shutdown(system)
  }


}
