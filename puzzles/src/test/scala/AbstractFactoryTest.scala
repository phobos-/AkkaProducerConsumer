import org.scalatest.{ FlatSpec, Matchers }

class AbstractFactoryTest extends FlatSpec with Matchers {

  abstract class AbstractFactory {
    type type1 <: T1
    type type2 <: T2

    def createT1(): type1
    def createT2(): type2

    abstract class T1
    abstract class T2
  }

  object Factory extends AbstractFactory {
    override type type1 = CT1
    override type type2 = CT2

    override def createT1(): type1 = new CT1

    override def createT2(): type2 = new CT2

    protected class CT1 extends T1
    protected class CT2 extends T2
  }

  "Factory" should "create two different instance types" in {
    Factory.createT1().getClass shouldNot equal(Factory.createT2().getClass)
  }
}
