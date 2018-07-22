import java.nio.file.Paths

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.log4j.Logger
import org.apache.log4j.Level

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements", "org.wartremover.warts.ToString"))
object Spark {

  System.setProperty("hadoop.home.dir", Paths.get(getClass.getResource("/bin").toURI).getParent.toString)
  Logger.getLogger("org").setLevel(Level.ERROR)
  Logger.getLogger("akka").setLevel(Level.ERROR)

  private lazy val spark: SparkSession = SparkSession.builder
    .master("local")
    .appName("Spark Pi")
    .getOrCreate()

  lazy val sc: SparkContext = spark.sparkContext

  def stop(): Unit = spark.stop
}
