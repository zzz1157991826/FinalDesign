import com.typesafe.config.ConfigFactory
import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, HBaseAdmin}
import org.apache.hadoop.hbase.mapred.TableOutputFormat

/**
  *
  * @ Description: 
  * @ Author: WangChunbo
  * @ Version: 1.0 
  * @ Date: 2019/03/07 17:16
  */
object Text {
  def main(args: Array[String]): Unit = {
    println("aaaaaaaaaaaaaaaaaaaaaaaaa")
    /**
      * HBASE连接
      */
    // 配置HBASE连接
    val load = ConfigFactory.load("application.conf")
//    val hbaseTableName = load.getString("hbase.table.name")
    val hbaseTableName = "aaaa"
    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.quorum",load.getString("hbase.zookeeper.host"))
    hbaseConf.set("hbase.zookeeper.property.clientPort", load.getString("hbase.zookeeper.clientPort"))
    hbaseConf.set(TableOutputFormat.OUTPUT_TABLE, hbaseTableName)
    // 去加载我们的连接
    val hbadmin = new HBaseAdmin(hbaseConf)

//    val table = new HTable(conf, "mytable")
    // 判断表是否存在
    println(s"$hbaseTableName 是否存在："+hbadmin.isTableAvailable(hbaseTableName))
    println("aaa 是否存在："+hbadmin.isTableAvailable("dmp:aaaa"))
    if(!hbadmin.isTableAvailable(hbaseTableName)){
      println("正在创建表！！！")
      // 创建表对象
      val tableDescriptor =
        new HTableDescriptor(TableName.valueOf(hbaseTableName))
      // 创建一个列簇
      val columnDescriptor = new HColumnDescriptor("tags")
      // 将列簇加入到表中
      tableDescriptor.addFamily(columnDescriptor)
      hbadmin.createTable(tableDescriptor)
    }
  }

}
