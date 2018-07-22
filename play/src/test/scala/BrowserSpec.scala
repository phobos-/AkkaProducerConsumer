import org.scalatestplus.play.guice.GuiceOneServerPerTest
import org.scalatestplus.play.{ HtmlUnitFactory, OneBrowserPerTest, PlaySpec, ServerProvider }

/**
 * Runs a browser test using Fluentium against a play application on a server port.
 */
class BrowserSpec
    extends PlaySpec
    with OneBrowserPerTest
    with GuiceOneServerPerTest
    with HtmlUnitFactory
    with ServerProvider {

  "Application" should {

    "work from within a browser" in {

      go to ("http://localhost:" + port)

      pageSource must include("Hello world!")
    }
  }
}
