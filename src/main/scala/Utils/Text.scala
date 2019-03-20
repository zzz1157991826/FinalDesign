package Utils

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

object Text {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("map").setMaster("local")
    val sc = new SparkContext(conf)
    val ssc = new SQLContext(sc)
    val df = ssc.read.json("D:\\gp1809\\考试题\\JsonTest02.json")
    df.map(t=>{
      t.getAs[String]("")
    })
  }
}
