package Tag

import org.apache.commons.lang.StringUtils
import org.apache.spark.sql.Row

import scala.collection.mutable

/**
  * 广告标签
  */
object TagsAd extends Tags {
  /**
    * 定义一个打标签的接口
    */
  override def makeTags(args: Any*): mutable.Map[String, (String, Int)] = {
    var map = mutable.Map[String, (String, Int)]()
    // 参数解析
    val row = args(0).asInstanceOf[Row]
    // 获取广告参数
    //adspacetype: Int,
    //adspacetypename: String,
    // 广告位类型（标签格式： LC03->1 或者 LC16->1）xx 为数字，
    // 小于 10 补 0，把广告位类型名称，LN 插屏->1
    val adType = row.getAs[Int]("adspacetype")
    adType match {
      case v if v > 9 =>map += ("adType" -> ("LC"+v, 1))
      case v if v > 0 && v <=9 => map += ("adType" -> ("LC0"+v,1))
    }
    val adName = row.getAs[String]("adspacetypename")
    if(StringUtils.isNotBlank(adName)){
      map += ("adName" -> ("LN"+adName,1))
    }
    // 渠道标签
    // （标签格式： CNxxxx->1）xxxx 为渠道 ID(adplatformproviderid)
    val channal = row.getAs[Int]("adplatformproviderid")
    map += ("channal" -> ("CN"+channal,1))
    map
  }
}
