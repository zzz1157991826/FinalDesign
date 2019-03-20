package Tag

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.Row

import scala.collection.mutable

/**
  * 关键字标签
  */
object TagsKeyWord extends Tags {
  /**
    * 定义一个打标签的接口
    */
  override def makeTags(args: Any*): mutable.Map[String, (String, Int)] = {
    var map = mutable.Map[String, (String, Int)]()
    // 获取数据文件，然后再进处理
    val row =args(0).asInstanceOf[Row]
    val stopwords = args(1).asInstanceOf[Broadcast[Map[String, Int]]]
    // 首先处理数据
    /**
      * 5)关键字（标签格式：Kxxx->1）xxx 为关键字，关键字长度不能少于 3 个字符，且不能
      超过 8 个字符；关键字中如包含‘‘|’’，则分割成数组，转化成多个关键字标签
      */
    row.getAs[String]("keywords").split("\\|").filter(
      // 进行数据的过滤，过滤出需要的数据
      word=>word.length>=3 && word.length<=8 &&
        !stopwords.value.keySet.contains(word)
    ).foreach(word=>map+=("word" -> ("K"+word,1)))
    map
  }
}
