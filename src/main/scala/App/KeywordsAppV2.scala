package App

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object KeywordsAppV2 {
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

    val text: RDD[String] = sc.textFile(inputPath)

    //                      lines.flatMap(_.split(" "))
//    val words: RDD[String] = text.flatMap(_.split("|"))
//
//    // 把每个单词生成一个一个的tuple
//    val tuples = words.map((_, 1))
//
//    // 以key(单词)进行分组
//    val sumed = tuples.reduceByKey(_+_)
//
//    val sorted = sumed.sortBy(x => x._2, false)



    println(text.collect.toBuffer)
  }
}
