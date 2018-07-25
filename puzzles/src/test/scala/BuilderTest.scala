import org.scalatest.{ FlatSpec, Matchers }

class BuilderTest extends FlatSpec with Matchers {

  class Thing(thing1: String, thing2: Int)
  case class ThingBuilder() {
    var thing1: String = ""
    var thing2: Int    = 0

    def withThing1(t: String): ThingBuilder = {
      thing1 = t
      this
    }

    def withThing2(t: Int): ThingBuilder = {
      thing2 = t
      this
    }

    def build(): Thing = new Thing(thing1, thing2)
  }

  "Thing" should "be built using builder pattern" in {
    ThingBuilder().withThing1("some").withThing2(2).build().getClass should equal(classOf[Thing])
  }
}
