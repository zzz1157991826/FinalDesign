package ETL

import Utils.PhoneNumber
import org.apache.hadoop.io.compress.BZip2Codec
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

/**
  *
  * @ Description: 为原始数据加上手机号字段
  * @ Author: WangChunbo
  * @ Version: 1.0 
  * @ Date: 2019/03/09 17:49
  */
object UserId {
  def main(args: Array[String]): Unit = {
    // 模拟企业开发模式,首先判断一下目录是否为空
    if (args.length != 2) {
      println("目录不正确,退出程序!")
      sys.exit()
    }
    // 创建一个集合,存储一下输入输出目录
    val Array(inputPath, outputPath) = args
    val conf = new SparkConf()
      .setAppName(this.getClass.getName).setMaster("local[2]")
      // 处理数据,采用scala的序列化方式,性能比Java默认的高
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc = new SparkContext(conf)
    //sc.hadoopConfiguration.addResource("cluster/core-site.xml")
    //sc.hadoopConfiguration.addResource("cluster/hdfs-site.xml")
    // 我们要采用snappy压缩方式,因为使用的是1.6的spark,如果到2.0以后就是默认的了,可以不用配置
    val sqlContext = new SQLContext(sc)
    sqlContext.setConf("spark.io.compression.snappy.codec", "snappy")

    //对数据进添加模拟手机号操作，将手机号添加到原始数据的结尾
    sc.textFile(inputPath).map(line => {
      val pn = PhoneNumber.getTel
      line + "," + pn
    })
      .repartition(1)
      .saveAsTextFile(outputPath, classOf[BZip2Codec])

    sc.stop()
  }

}
