//package Tag
//
//import ch.hsr.geohash.GeoHash
//import org.apache.commons.lang.StringUtils
//import org.apache.spark.sql.Row
//import redis.clients.jedis.Jedis
//
///**
//  * 商圈标签
//  */
//object TagsBusiness extends Tags {
//  /**
//    * 定义一个打标签的接口
//    */
//  override def makeTags(args: Any*): List[(String, Int)] = {
//    val row =args(0).asInstanceOf[Row]
//    val jedis = args(1).asInstanceOf[Jedis]
//    var list= List[(String,Int)]()
//    // 进行过滤操作 过滤出需要的经纬度
//    if(row.getAs[String]("long").toDouble >= 73
//      && row.getAs[String]("long").toDouble <= 136
//      && row.getAs[String]("lat").toDouble >=3
//      && row.getAs[String]("lat").toDouble <= 54){
//      // 取出经纬度
//      val long = row.getAs[String]("long")
//      val lat = row.getAs[String]("lat")
//      // 将经纬度转换成geohash码
//      val geoHash = GeoHash
//        .geoHashStringWithCharacterPrecision(lat.toDouble,long.toDouble,8)
//      // 进行取值
//      val geo = jedis.get(geoHash)
//      if(StringUtils.isNotBlank(geo)){
//        // 获取对应得商圈
//        geo.split(";").foreach(t=>list:+=(t,1))
//      }
//    }
//    list
//  }
//}
