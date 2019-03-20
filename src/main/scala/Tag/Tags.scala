package Tag

import scala.collection.mutable

trait Tags {
  /**
    * 定义一个打标签的接口
    */
  def makeTags(args:Any*):mutable.Map[String, (String, Int)]
}
