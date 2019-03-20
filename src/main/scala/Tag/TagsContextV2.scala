package Tag

import Utils.{JedisConnectionPool, TagsUtils}
import com.typesafe.config.ConfigFactory
import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, HBaseAdmin, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 得到当天的最终的用户标签
  */
object TagsContextV2 {
  def main(args: Array[String]): Unit = {
    // 模拟企业开发模式，首先判断一下目录 是否为空
    if(args.length != 3){
      println("目录不正确，退出程序！")
      sys.exit()
    }
    // 创建一个集合，存储一下输入输出目录
    val Array(inputPath,dirPath,stopWord) = args
    val conf = new SparkConf()
      .setAppName(this.getClass.getName).setMaster("local")
      // 处理数据，采取scala的序列化方式，性能比Java默认的高
      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
    val sc = new SparkContext(conf)
    //sc.hadoopConfiguration.addResource("cluster/core-site.xml")
    //sc.hadoopConfiguration.addResource("cluster/hdfs-site.xml")
    // 我们要采取snappy压缩方式， 因为咱们现在用的是1.6版本的spark，到2.0以后呢，就是默认的了
    // 可以不需要配置
    val sqlContext = new SQLContext(sc)
    sqlContext.setConf("spark.io.compression.snappy.codec","snappy")
    /**
      * HBASE连接
      */
      // 配置HBASE连接
    val load = ConfigFactory.load("application.conf")
    val hbaseTableName = load.getString("hbase.table.name")
    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.quorum",load.getString("hbase.zookeeper.host"))
    hbaseConf.set("hbase.zookeeper.property.clientPort", load.getString("hbase.zookeeper.clientPort"))
    hbaseConf.set(TableOutputFormat.OUTPUT_TABLE, hbaseTableName)
    // 去加载我们的连接
    val hbadmin = new HBaseAdmin(hbaseConf)
    // 判断表是否存在
    if(!hbadmin.tableExists(TableName.valueOf(hbaseTableName))){
      println("正在创建表！！！")
      // 创建表对象
      val tableDescriptor =
        new HTableDescriptor(TableName.valueOf(hbaseTableName))
      // 创建一个列簇
      val columnDescriptor = new HColumnDescriptor("tags")
      // 将列簇加入到表中
      tableDescriptor.addFamily(columnDescriptor)
      hbadmin.createTable(tableDescriptor)
    }
    // 创建一个jobconf任务
    val jobConf = new JobConf(hbaseConf)
    // 指定key的输出类型
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    // 指定输出到哪个表内
    jobConf.set(TableOutputFormat.OUTPUT_TABLE,hbaseTableName)


    // 读取字典文件
    val dirMap = sc.textFile(dirPath).map(_.split("\t",-1)).filter(_.length>=5)
      .map(arr=>{
        (arr(4),arr(1))
      }).collect.toMap
    // 广播字典文件
    val broadcast = sc.broadcast(dirMap)
    // 读取停用词库
    val stopwordDir = sc.textFile(stopWord).map((_,0)).collect.toMap
    // 广播停用词库
    val stopwords = sc.broadcast(stopwordDir)
    // 读取文件数据
    val baseRDD = sqlContext.read.parquet(inputPath)
    // 构建标签并存入Hbase中
    baseRDD.map(row => {
      // 获取用户手机号
      val phoneNumber = row.getAs[String]("phonenum")
      // 标签处理
      val adTag = TagsAd.makeTags(row)
      val appTag = TagsAPP.makeTags(row, broadcast)
      val deviceTag = TagsDevice.makeTags(row)
      val keywordTag = TagsKeyWord.makeTags(row, stopwords)
      val LocationTag = TagsLocation.makeTags(row)
      // 这块商圈标签先不加，因为里面没数据
      //      val tagsBusiness = TagsBusiness.makeTags(row,jedis)
      val put = new Put(Bytes.toBytes(phoneNumber))
      // 将所有的数据输入到HBASE
      adTag.map(tup => {
        put.addImmutable(Bytes.toBytes("tags"),Bytes.toBytes(tup._1.toString),Bytes.toBytes(tup._2._1.toString + tup._2._2))
      })
      appTag.map(tup => {
        put.addImmutable(Bytes.toBytes("tags"),Bytes.toBytes(tup._1.toString),Bytes.toBytes(tup._2._1.toString + tup._2._2))
      })
      deviceTag.map(tup => {
        put.addImmutable(Bytes.toBytes("tags"),Bytes.toBytes(tup._1.toString),Bytes.toBytes(tup._2._1.toString + tup._2._2))
      })
      keywordTag.map(tup => {
        put.addImmutable(Bytes.toBytes("tags"),Bytes.toBytes(tup._1.toString),Bytes.toBytes(tup._2._1.toString + tup._2._2))
      })
      LocationTag.map(tup => {
        put.addImmutable(Bytes.toBytes("tags"),Bytes.toBytes(tup._1.toString),Bytes.toBytes(tup._2._1.toString + tup._2._2))
      })
      // HBASE返回所有的数据
      (new ImmutableBytesWritable(),put)
    })
        .saveAsHadoopDataset(jobConf)


    hbadmin.close()
    sc.stop()
  }
}
