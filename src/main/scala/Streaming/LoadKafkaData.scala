package Streaming

import org.apache.spark.{SparkConf}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object LoadKafkaData {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().
      setAppName(this.getClass.getName).setMaster("local[2]")
    val ssc = new StreamingContext(conf,Seconds(5))

    //设置检查点
    //ssc.checkpoint("c://cp-20190412-0")

    // 配置用于请求kafka的几个参数
    val Array(zkQuorum, group, topics, numThreads) = args

    //把每一个topic放到一个map里
    val topicMap: Map[String, Int] =
      topics.split(",").map((_,numThreads.toInt)).toMap

    //调用kafka的工具类来获取topic的数据
    val dStream: ReceiverInputDStream[(String, String)] =
      KafkaUtils.createStream(ssc, zkQuorum, group, topicMap)

    // dStream中，key的数据为offset，value为实际的数据
    // 把数据获取到
    val lines: DStream[String] = dStream.map(_._2)

    // 开始分析数据
    val value: DStream[String] = lines.map(line => {
      val strings = line.split("\\?")
      // 数组怎么取值啊。。
      val url = strings(0)
      val prarems = strings(1)
      val prarem = prarems.split("\\&")
      val p1 = prarem(0)
      val p2 = prarem(1)
      val p3 = prarem(2)
      val p4 = prarem(3)
      val p5 = prarem(4)

      p5.split("\\=")(1) //获取到的就是phonenumber
    })
    value.print()//打印
    //得到了value即为此时登录用户的phonenumber
    //得到hbase中dmp:dmp表中数据

    //将phoneNumber与dmp:dmp表中每一行数据的行键phoneNumber进行比较，取出行键相同的那一行用户的标签

    ssc.start()
    ssc.awaitTermination()
  }

}
