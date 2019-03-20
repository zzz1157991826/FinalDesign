package Streaming

import com.typesafe.config.ConfigFactory
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  *
  * @ Description: kafka测试类
  * @ Author: WangChunbo
  * @ Version: 1.0 
  * @ Date: 2019/03/10 18:03
  */
object KafkaText {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("KafkaText").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(2))
    //    ssc.checkpoint("f://kafkacheckpoint")
    val load = ConfigFactory.load("application.conf")
    val broker = Map("metadata.broker.list" -> load.getString("metadata.broker.list"))
    val topics = Set(load.getString("kafka.topic1"))

    val dStream: DStream[String] =
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, broker, topics).map(_._2)

    dStream.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
