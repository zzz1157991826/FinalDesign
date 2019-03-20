package Utils

/**
  *
  * @ Description: 
  * @ Author: WangChunbo
  * @ Version: 1.0 
  * @ Date: 2019/03/09 17:53
  */
object PhoneNumber {
  /**
    * 返回手机号码
    */
  private val telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",")

  def getNum(start: Int, end: Int): Int = (Math.random * (end - start + 1) + start).toInt

  def getTel = {
    val index = getNum(0, telFirst.length - 1)
    val first = telFirst(index)
    val second = String.valueOf(getNum(1, 888) + 10000).substring(1)
    val third = String.valueOf(getNum(1, 9100) + 10000).substring(1)
    first + second + third
  }
}
