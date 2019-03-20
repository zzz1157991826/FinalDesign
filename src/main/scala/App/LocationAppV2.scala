package App

import java.util.Properties

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 地域纬度指标
  * Spark core实现(自己实现)
  */
object LocationAppV2 {
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      println("参数数量错误,请重新输入!")
      sys.exit()
    }

    val conf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[2]")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    sqlContext.setConf("spark.io.compression.snappy.codec", "snappy")

    val Array(inputPath) = args
    val df = sqlContext.read.parquet(inputPath)

    val dataRDD = df.map(x => {
      ((x.getAs[String]("provincename"), x.getAs[String]("cityname")),
        Array(
          x.getAs[Int]("requestmode"),
          x.getAs[Int]("processnode"),
          x.getAs[Int]("iseffective"),
          x.getAs[Int]("isbilling"),
          x.getAs[Int]("isbid"),
          x.getAs[Int]("iswin"),
          x.getAs[Int]("adorderid"),
          x.getAs[Double]("winprice"),
          x.getAs[Double]("adpayment"))
      )
    })

    val resRDD = dataRDD.groupByKey().map(x => {
      var ysrequest = 0
      var yxrequest = 0
      var adrequest = 0
      var cybid = 0
      var cybidsuccess = 0
      var shows = 0
      var clicks = 0
      var dspcost = 0.0
      var dspapy = 0.0

      x._2.map(arr => {
        if (arr(0) == 1 && arr(1) >= 1) {
          ysrequest += 1
        }
        if (arr(0) == 1 && arr(1) >= 2) {
          yxrequest += 1
        }
        if (arr(0) == 1 && arr(1) >= 3) {
          adrequest += 1
        }
        if (arr(2) == 1 && arr(3) == 1 && arr(4) == 1) {
          cybid += 1
        }
        if (arr(2) == 1 && arr(3) == 1 && arr(5) == 1 && arr(6) != 0) {
          cybidsuccess += 1
        }
        if (arr(0) == 2 && arr(2) == 1) {
          shows += 1
        }
        if (arr(0) == 3 && arr(2) == 1) {
          clicks += 1
        }
        if (arr(2) == 1 && arr(3) == 1 && arr(5) == 1) {
          dspcost += arr(7) / 1000.0
          dspapy += arr(8) / 1000.0
        }
      })
      (x._1._1, x._1._2, ysrequest, yxrequest, adrequest, cybid, cybidsuccess, shows, clicks, dspcost, dspapy)
    })
    resRDD

    import sqlContext.implicits._
    val resDF: DataFrame = resRDD.map(x => Location(x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11)).toDF()

    //存入数据库
    val load = ConfigFactory.load()
    val props = new Properties()
    props.put("user", load.getString("jdbc.user"))
    props.put("password", load.getString("jdbc.password"))
    props.put("driver", "com.mysql.jdbc.Driver")

    resDF.write.mode(SaveMode.Append).jdbc(
      load.getString("jdbc.url"), load.getString("jdbc.tbn"), props)


    sc.stop()

  }
}

case class Location(provincename: String, cityname: String, requestmode: Int, processnode: Int, iseffective: Int, isbilling: Int, isbid: Int, iswin: Int, adordeerid: Int, winprice: Double, adpayment: Double)