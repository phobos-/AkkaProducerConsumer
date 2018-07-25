import org.scalatest.{ FlatSpec, Matchers }

class VisitorTest extends FlatSpec with Matchers {
  trait Visitor {
    def visit(): Int
  }

  trait Visitee {
    def accept(v: Visitor): Int
  }

  object Visitor1 extends Visitor {
    override def visit(): Int = 1
  }

  object Visitor2 extends Visitor {
    override def visit(): Int = 2
  }

  object Object1 extends Visitee {
    override def accept(v: Visitor): Int = v.visit()
  }

  object Object2 extends Visitee {
    override def accept(v: Visitor): Int = v.visit()
  }

  "Object1" should "accept visitors with different outcome" in {
    Object1.accept(Visitor1) should equal(1)
    Object1.accept(Visitor2) should equal(2)
  }

  "Object2" should "accept visitors with different outcome" in {
    Object2.accept(Visitor1) should equal(1)
    Object2.accept(Visitor2) should equal(2)
  }
}
