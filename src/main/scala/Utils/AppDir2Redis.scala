package Utils

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

/**
  * 处理字典文件数据，将其存入Redis中
  */
object AppDir2Redis {
  def main(args: Array[String]): Unit = {
    // 模拟企业开发模式，首先判断一下目录 是否为空
    if(args.length != 1){
      println("目录不正确，退出程序！")
      sys.exit()
    }
    // 创建一个集合，存储一下输入输出目录
    val Array(inputPath) = args
    val conf = new SparkConf()
      .setAppName(this.getClass.getName).setMaster("local")
      // 处理数据，采取scala的序列化方式，性能比Java默认的高
      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
    val sc = new SparkContext(conf)
    // 我们要采取snappy压缩方式， 因为咱们现在用的是1.6版本的spark，到2.0以后呢，就是默认的了
    // 可以不需要配置
    val sqlContext = new SQLContext(sc)
    sqlContext.setConf("spark.io.compression.snappy.codec","snappy")
    // 读取字典文件数据
    val file = sc.textFile(inputPath)
    file.map(_.split("\t",-1)).filter(_.length>=5)
      .map(arr=>(arr(4),arr(1)))
      // 调用foreachPartition，主要是为了减少创建jedis的次数
      // 此时创建Jedis的次数和partition数量相等
      .foreachPartition(ite=>{
      // 创建连接 拿到jedis对象
        val jedis = JedisConnectionPool.getConnection()
        ite.foreach(f=>{
          jedis.set(f._1,f._2)
        })
      jedis.close()
      })
    sc.stop()
  }
}
