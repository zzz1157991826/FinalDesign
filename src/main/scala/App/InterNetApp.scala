package App

import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 终端设备维度网络指标
  */
object InterNetApp {
  def main(args: Array[String]): Unit = {
    if (args.length != 2){
      println("参数数量错误,请重新输入!")
      sys.exit()
    }
    val conf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val Array(inputPath,outputPath) = args

    val fileDF = sqlContext.read.parquet(inputPath)
    fileDF.registerTempTable("log")

    val resDF = sqlContext.sql(
      """
        |select networkmannername,
        |sum(case when requestmode = 1 and processnode >= 1 then 1 else 0 end) ysrequest,
        |sum(case when requestmode = 1 and processnode >= 2 then 1 else 0 end) yxrequest,
        |sum(case when requestmode = 1 and processnode >= 3 then 1 else 0 end) adrequest,
        |sum(case when iseffective = 1 and isbilling = 1 and isbid = 1 then 1 else 0 end) cybid,
        |sum(case when iseffective = 1 and isbilling = 1 and iswin = 1 and adorderid != 0 then 1 else 0 end) cybidsuccess,
        |sum(case when requestmode = 2 and iseffective = 1 then 1 else 0 end) shows,
        |sum(case when requestmode = 3 and iseffective = 1 then 1 else 0 end) clicks,
        |sum(case when iseffective = 1 and isbilling = 1 and iswin = 1 then winprice/1000 else 0 end) dspcost,
        |sum(case when iseffective = 1 and isbilling = 1 and iswin = 1 then adpayment/1000 else 0 end) dspapy
        |from log group by networkmannername
      """.stripMargin)

    resDF.repartition(1).write.mode(SaveMode.Append).json(outputPath)
    //    //存入数据库
    //    val load = ConfigFactory.load()
    //    val props = new Properties()
    //    props.put("user", load.getString("jdbc.user"))
    //    props.put("password", load.getString("jdbc.password"))
    //    props.put("driver", "com.mysql.jdbc.Driver")
    //
    //    resDF.write.mode(SaveMode.Append).jdbc(
    //      load.getString("jdbc.url"), "operator", props)

    sc.stop()
  }
}
