object SparkPi extends App {
  val iterations = 100000000L
  val result     = PiCalculation(Spark.sc).calculatePi(iterations)
  print("Pi is roughly " + result)
  Spark.stop()
}
