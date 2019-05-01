package Netflix.SR

import org.apache.spark.sql.{SaveMode, SparkSession}
import java.util.logging.{Level, Logger}
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.rdd.RDD



object Run {

  def main(args: Array[String]): Unit = {

    val logger = Logger.getLogger(getClass.getName)
    val p = new Prop
    val spark = SparkSession.builder
      .appName(p.getProp("APP_NAME"))
      .getOrCreate()

    lazy val ratingDF = spark.read.format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(p.getProp("PATH_ratingDF_HDFS"))


    lazy val moviesDF = spark.read.format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(p.getProp("PATH_movieID_HDFS"))



    val SplitRating = ratingDF.randomSplit(Array(0.75, 0.25), seed = 1234L)
    val (trainingData, testData) = (SplitRating(0), SplitRating(1))
    val numTraining = trainingData.count()
    val numTest = testData.count()



    val ratingsRDD = trainingData.rdd.map(row => {
      val userId = row.getInt(0)
      val movieId = row.getInt(1)
      val ratings = row.getDouble(2)
      Rating(userId, movieId, ratings)

    })


    val testRDD = testData.rdd.map(row => {

      val userId = row.getInt(0)

      val movieId = row.getInt(1)

      val ratings = row.getDouble(2)

      Rating(userId, movieId, ratings)

    })



    lazy val model = new ALS()

      .setIterations(   p.getProp( "numIterations").toInt  )
      .setBlocks(p.getProp("block").toInt )
      .setAlpha(p.getProp("alpha").toDouble )
      .setLambda( p.getProp("lambda").toDouble )
      .setRank(p.getProp("rank").toInt )
      .setSeed(p.getProp("seed").toLong )
      .setImplicitPrefs(   p.getProp("implicitPrefs").toBoolean )
      .run(ratingsRDD)



    val predictions: RDD[Rating] = model.predict(testRDD.map(x => (x.user, x.product)))


    import spark.implicits._

    val df = predictions.toDF

    df.repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      .format("csv")
      .save(  p.getProp("PATH_OUTPUT_HDFS")  )


  }

}
