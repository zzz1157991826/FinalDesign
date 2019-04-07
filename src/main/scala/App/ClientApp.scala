package App

import java.util.Properties

import com.typesafe.config.ConfigFactory
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SaveMode}

/**
  * 设备类型分析
  */
object ClientApp {
  def main(args: Array[String]): Unit = {
    //模拟企业开发模式，首先判断目录是否为空
    if(args.length != 1){
      println("目录不正确，退出程序!")
      sys.exit()
    }

    //创建一个集合，存储输入输出目录
    val Array(inputPath) = args
    val conf = new SparkConf()
      .setAppName(this.getClass.getName).setMaster("local")
      //处理数据，采取Scala的序列化方式，性能比Java默认的高
      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
    val sc = new SparkContext(conf)
    //我们要采取snappy压缩方式，因为咱们现在采用的是1.6版本的spark到2.0以后就是默认的
    //可以不用配置
    val sqlContext = new SQLContext(sc)
    sqlContext.setConf("spark.io.compression.snappy.codec","snappy")
    val df = sqlContext.read.parquet(inputPath)
    df.registerTempTable("log")
    //通过SQL处理数据文件
    val result = sqlContext.sql("select client,count(*) clientct " +
      "from log group by client")
    // 接下来我们将数据进行存储到Mysql中，需要加载application.conf文件中的配置项
    val load = ConfigFactory.load()
    val props = new Properties()
    props.setProperty("user",load.getString("jdbc.user"))
    props.setProperty("password",load.getString("jdbc.password"))
    result.write.mode(SaveMode.Append).jdbc(
      load.getString("jdbc.url"),load.getString("jdbc.tbn4"),props)


    sc.stop()
  }
}
