package ETL

import Utils.{SchemaUtils, Tools}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * DMP中清洗数据
  * 处理HDFS原始数据文件转换成 Parquet
  */
object Bz2Parquet {
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


    // 读取数据文件
    val lines = sc.textFile(inputPath)

    val rowRDD = lines.map(x => x.split(",", -1)).filter(x => x.length >= 85)
      .map(arr => {
        Row(
          arr(0),
          Tools.toInt(arr(1)),
          Tools.toInt(arr(2)),
          Tools.toInt(arr(3)),
          Tools.toInt(arr(4)),
          arr(5),
          arr(6),
          Tools.toInt(arr(7)),
          Tools.toInt(arr(8)),
          Tools.toDouble(arr(9)),
          Tools.toDouble(arr(10)),
          arr(11),
          arr(12),
          arr(13),
          arr(14),
          arr(15),
          arr(16),
          Tools.toInt(arr(17)),
          arr(18),
          arr(19),
          Tools.toInt(arr(20)),
          Tools.toInt(arr(21)),
          arr(22),
          arr(23),
          arr(24),
          arr(25),
          Tools.toInt(arr(26)),
          arr(27),
          Tools.toInt(arr(28)),
          arr(29),
          Tools.toInt(arr(30)),
          Tools.toInt(arr(31)),
          Tools.toInt(arr(32)),
          arr(33),
          Tools.toInt(arr(34)),
          Tools.toInt(arr(35)),
          Tools.toInt(arr(36)),
          arr(37),
          Tools.toInt(arr(38)),
          Tools.toInt(arr(39)),
          Tools.toDouble(arr(40)),
          Tools.toDouble(arr(41)),
          Tools.toInt(arr(42)),
          arr(43),
          Tools.toDouble(arr(44)),
          Tools.toDouble(arr(45)),
          arr(46),
          arr(47),
          arr(48),
          arr(49),
          arr(50),
          arr(51),
          arr(52),
          arr(53),
          arr(54),
          arr(55),
          arr(56),
          Tools.toInt(arr(57)),
          Tools.toDouble(arr(58)),
          Tools.toInt(arr(59)),
          Tools.toInt(arr(60)),
          arr(61),
          arr(62),
          arr(63),
          arr(64),
          arr(65),
          arr(66),
          arr(67),
          arr(68),
          arr(69),
          arr(70),
          arr(71),
          arr(72),
          Tools.toInt(arr(73)),
          Tools.toDouble(arr(74)),
          Tools.toDouble(arr(75)),
          Tools.toDouble(arr(76)),
          Tools.toDouble(arr(77)),
          Tools.toDouble(arr(78)),
          arr(79),
          arr(80),
          arr(81),
          arr(82),
          arr(83),
          Tools.toInt(arr(84)),
          arr(85)
        )
      })
    val df = sqlContext.createDataFrame(rowRDD, SchemaUtils.schema)
    df.write.parquet(outputPath)

    sc.stop()
  }
}
