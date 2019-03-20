package App

import Utils.LocationUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 媒体纬度指标
  * 使用Spark core实现
  */
object MediaApp {
  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      println("参数数量错误,请重新输入!")
      sys.exit()
    }
    val conf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[2]")
      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    sqlContext.setConf("spark.io.compression.snappy.codec","snappy")
    val Array(inputPath, outputPath) = args

    // 获取url和商家名对照表
    val tmpFile: RDD[String] = sc.textFile("F:\\9-项目\\DMP广告项目\\DMP项目文档\\app_dict.txt")

    // 过滤对照表
    val tmpRDD = tmpFile.map(_.split("\t")).filter(_.length >= 5)
      // URL(可能含有脏数据,需要清洗), 商家名称
      .map(x => (x(4), x(1))).filter(_._1.matches("[a-zA-Z]+\\..*"))

    // 将对照表广播到每个worker中去
    val tmpData = sc.broadcast(tmpRDD.collect().toMap)

    // 读取原始数据文件
    val df = sqlContext.read.parquet(inputPath)

    // 通过调用算子的方式处理数据
    df.map(x => {
      // 先去获取需要的数据,原始 有效 广告.....
      val requestmode = x.getAs[Int]("requestmode")
      val processnode = x.getAs[Int]("processnode")
      val iseffective = x.getAs[Int]("iseffective")
      val isbilling = x.getAs[Int]("isbilling")
      val isbid = x.getAs[Int]("isbid")
      val iswin = x.getAs[Int]("iswin")
      val adorderid = x.getAs[Int]("adorderid")
      val winprice = x.getAs[Double]("winprice")
      val adpayment = x.getAs[Double]("adpayment")
      // 此时,我们拿到了所有的数据了,那么如何处理?
      // 写一个工具类,然后使用集合的方式进行处理
      val reqlist = LocationUtils
        .requestUtils(requestmode, processnode)
      val aslist = LocationUtils
        .requestAD(iseffective, isbilling, isbid, iswin, adorderid, winprice, adpayment)
      val clicklist = LocationUtils.requestShow(requestmode, iseffective)

      //获取15列的应用名称
      var appUrl = x.getAs[String](13)
      var appName = x.getAs[String](14)

      if (appName.length == 0){
        // 过滤脏数据
        appName = if (appUrl.length == 0) {
          "UnKnow"
        } else if (appUrl.matches("[a-zA-z]+\\..*")) {
          tmpData.value.getOrElse(appUrl, "UnKnow")
        } else {
          appUrl
        }
      }



      (appName, reqlist ++ aslist ++ clicklist)
    })
      .reduceByKey((list1, list2) => {
        list1.zip(list2).map(x => x._1 + x._2)
      })
      .map(t => t._1 + "," + t._2.mkString(","))
      // 存入hdfs
      .saveAsTextFile(outputPath)

    sc.stop()
  }
}
