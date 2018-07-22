import org.apache.spark.SparkContext

import scala.math.random

class PiCalculation(sparkContext: SparkContext) {
  def calculatePi(iterations: Long, slices: Int = Runtime.getRuntime.availableProcessors): Double = {
    val n: Int = math.min(iterations * slices, Int.MaxValue).toInt // avoid overflow
    val count: Int = sparkContext
      .parallelize(1 until n, slices)
      .map { _ =>
        val x = random * 2 - 1
        val y = random * 2 - 1
        if (x * x + y * y <= 1) 1 else 0
      }
      .reduce(_ + _)

    4.0 * count / (n - 1)
  }
}

object PiCalculation {
  def apply(sparkContext: SparkContext): PiCalculation = new PiCalculation(sparkContext)
}
