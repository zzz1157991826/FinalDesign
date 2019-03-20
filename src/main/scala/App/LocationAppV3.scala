package App

import Utils.LocationUtils
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 地域纬度指标
  * Spark core实现(讲师实现)
  */
object LocationAppV3 {
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      println("参数数量错误,请重新输入!")
      sys.exit()
    }

    val conf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    sqlContext.setConf("spark.io.compression.snappy.codec", "snappy")

    val Array(inputPath) = args
    val df = sqlContext.read.parquet(inputPath)

    //通过调用算子的方式处理数据
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
        .requestUtils(requestmode,processnode)
      val aslist = LocationUtils
        .requestAD(iseffective,isbilling,isbid,iswin,adorderid,winprice,adpayment)
      val clicklist = LocationUtils.requestShow(requestmode,iseffective)
      ((x.getAs[String]("provincename"), x.getAs[String]("cityname")),
      reqlist ++ aslist ++ clicklist)
    })

  }
}
