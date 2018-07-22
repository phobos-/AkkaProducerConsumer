import org.scalatest.{ BeforeAndAfterAll, FlatSpec, Matchers }

class SparkPiTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  "A PiCalculator" should "estimate pi to two decimal places" in {
    val iterations = 1000000L
    val result     = PiCalculation(Spark.sc).calculatePi(iterations)
    roundAt(result, 2) should equal(3.14)
  }

  override def afterAll(): Unit = Spark.stop()

  private def roundAt(n: Double, p: Int): Double = { val s = math pow (10, p); (math round n * s) / s }
}
