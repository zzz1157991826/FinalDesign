package Tag

import org.apache.commons.lang.StringUtils
import org.apache.spark.sql.Row

import scala.collection.mutable

/**
  * 地域标签
  */
object TagsLocation extends Tags {
  /**
    * 定义一个打标签的接口
    */
  override def makeTags(args: Any*): mutable.Map[String, (String, Int)] = {
    var map = mutable.Map[String, (String, Int)]()
    val row = args(0).asInstanceOf[Row]
    //  取出对应得数据
    val provincename = row.getAs[String]("provincename")
    val cityname = row.getAs[String]("cityname")
    // 做一下判断
    if(StringUtils.isNotBlank(provincename)){
      map+=("provincename" -> ("ZP"+provincename,1))
    }
    if(StringUtils.isNotBlank(cityname)){
      map+=("cityname" -> ("ZC"+cityname,1))
    }
    map
  }
}
