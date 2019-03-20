package Tag

import org.apache.spark.sql.Row

import scala.collection.mutable

/**
  * 设备标签
  */
object TagsDevice extends Tags {
  /**
    * 定义一个打标签的接口
    */
  override def makeTags(args: Any*): mutable.Map[String, (String, Int)] = {
    var map = mutable.Map[String, (String, Int)]()
    val row = args(0).asInstanceOf[Row]
    // 操作系统
    val client = row.getAs[Int]("client")

    /**
      * 设备操作系统
        1 Android D00010001
        2 IOS D00010002
        3 WinPhone D00010003
        _ 其 他 D00010004

        设 备 联 网 方 式
        WIFI D00020001
        4G D00020002
        3G D00020003
        2G D00020004
        _   D00020005

        设备运营商方式
        移 动 D00030001 联 通 D00030002 电 信 D00030003
        _ D00030004
      */
    client match {
      case 1 =>map+=("client" -> ("Android D00010001",1))
      case 2 =>map+=("client" -> ("IOS D00010002",1))
      case 3 =>map+=("client" -> ("WinPhone D00010003",1))
      case _ =>map+=("client" -> ("其 他 D00010004",1))
    }
    // 设备联网方式
    val network = row.getAs[String]("networkmannername")
    network match{
      case "Wifi" =>map+=("network" -> ("WIFI D00020001",1))
      case "4G" =>map+=("network" -> ("4G D00020002",1))
      case "3G" =>map+=("network" -> ("3G D00020003",1))
      case "2G" =>map+=("network" -> ("2G D00020004",1))
      case _ =>map+=("network" -> ("其他 D00020005",1))
    }
    // 移动运营商
    val ispid = row.getAs[String]("ispname")
    ispid match {
      case "移动" =>map+=("ispid" -> ("移 动 D00030001",1))
      case "联通" =>map+=("ispid" -> ("联 通 D00030002",1))
      case "电信" =>map+=("ispid" -> ("电 信 D00030003",1))
      case _ =>map+=("ispid" -> ("其他 D00030004",1))
    }
    map
  }
}
