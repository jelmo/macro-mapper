package macromapper.tests

import macromapper.TraceMacros._

object TraceTest {
  def main(args: Array[String]) {
    val x = 10

    trace(x + 3)
  }
}
