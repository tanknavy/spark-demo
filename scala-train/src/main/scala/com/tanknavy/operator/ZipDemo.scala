package com.tanknavy.operator

/**
 * Author: Alex Cheng 9/19/2020 1:50 PM
 */

//拉链(合并)
//1.结果中每个元素是Tuple2元祖，如果两个集合个数不对等会数据丢失
object ZipDemo {
  def main(args: Array[String]): Unit = {
    val list1 = List(11,12,13,14,15)
    val list2 = List("a","b","c")

    val list3 = list1.zip(list2)
    println(list3)
  }
}
