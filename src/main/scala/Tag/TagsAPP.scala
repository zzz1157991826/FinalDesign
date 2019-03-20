package Tag

import org.apache.commons.lang.StringUtils
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.Row

import scala.collection.mutable

/**
  * APP标签
  */
object TagsAPP extends Tags {
  /**
    * 定义一个打标签的接口
    */
  override def makeTags(args: Any*): mutable.Map[String, (String, Int)] = {
    var map = mutable.Map[String, (String, Int)]()
    // 获取Row类型数据
    val row = args(0).asInstanceOf[Row]
    // 获取我们字典文件
    val appdir = args(1).asInstanceOf[Broadcast[Map[String, String]]]
    // 获取appname appid
    val appname = row.getAs[String]("appname")
    val appid = row.getAs[String]("appid")
    // 进行空值判断
    if(StringUtils.isBlank(appname)){
      map+=("appid" -> ("APP"+appdir.value.getOrElse(appid,"未知"),1))
    }else{
      map+=("appname" -> ("APP"+appname,1))
    }
    map
  }
}
