import org.scalatest.{ FlatSpec, Matchers }

class DecoratorTest extends FlatSpec with Matchers {

  class Object {
    def action(): String = "Action"
  }

  trait Decorator extends Object {
    override def action(): String         = super.action() + decoratedAction()
    private def decoratedAction(): String = "Decorated"
  }

  "Object" should "get decorated" in {
    val decoratedObject = new Object with Decorator
    decoratedObject.action() should equal("ActionDecorated")
  }
}
