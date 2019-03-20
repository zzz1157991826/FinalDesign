//package Tag
//
//import Utils.TagsUtils
//import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.spark.sql.SQLContext
//
///**
//  * 创建上下文表，用于合并所有标签
//  */
//object TagsContext {
//  def main(args: Array[String]): Unit = {
//    // 模拟企业开发模式，首先判断一下目录 是否为空
//    if(args.length != 4){
//      println("目录不正确，退出程序！")
//      sys.exit()
//    }
//    // 创建一个集合，存储一下输入输出目录
//    val Array(inputPath,outputPath,dirPath,stopWord) = args
//    val conf = new SparkConf()
//      .setAppName(this.getClass.getName).setMaster("local")
//      // 处理数据，采取scala的序列化方式，性能比Java默认的高
//      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
//    val sc = new SparkContext(conf)
//    // 我们要采取snappy压缩方式， 因为咱们现在用的是1.6版本的spark，到2.0以后呢，就是默认的了
//    // 可以不需要配置
//    val sqlContext = new SQLContext(sc)
//    sqlContext.setConf("spark.io.compression.snappy.codec","snappy")
//    // 读取字典文件
//    val dirMap = sc.textFile(dirPath).map(_.split("\t",-1)).filter(_.length>=5)
//      .map(arr=>{
//        (arr(4),arr(1))
//      }).collect.toMap
//    // 广播字典文件
//    val broadcast = sc.broadcast(dirMap)
//    // 读取停用词库
//    val stopwordDir = sc.textFile(stopWord).map((_,0)).collect.toMap
//    // 广播停用词库
//    val stopwords = sc.broadcast(stopwordDir)
//    // 读取文件数据
//    sqlContext.read.parquet(inputPath)
//      // 过滤数据ID
//      .filter(TagsUtils.UserId)
//      // 根据每一条数据需求，进行打标签（6种标签）
//      .map(row=>{
//      // 首先先获取用户id
//      val userid = TagsUtils.getAnyOneUserId(row)
//      // 开始整理标签
//      val adTag = TagsAd.makeTags(row)
//      val appTag = TagsAPP.makeTags(row,broadcast)
//      val deviceTag = TagsDevice.makeTags(row)
//      val keywordTag = TagsKeyWord.makeTags(row,stopwords)
//      val LocationTag = TagsLocation.makeTags(row)
//      (userid,adTag++appTag++deviceTag++keywordTag++LocationTag)
//      // (xxxx,List((lN 爱奇艺，1),(K 爱情电影，1),(WiFi D00001，1)....))
//    }).reduceByKey((list1,list2)=>(list1:::list2)
//        .groupBy(_._1)//(ln 爱奇艺,List(1,1,1,1,1,0))
//        .mapValues(_.foldLeft[Int](0)(_+_._2))
//        .toList
//    ).map(t=>{
//      t._1+","+t._2.map(x=>x._1+","+x._2).mkString(",")
//    }).saveAsTextFile(outputPath)
//  }
//}
