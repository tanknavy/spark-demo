package com.tanknavy.operator

/**
 * Author: Alex Cheng 9/18/2020 10:24 PM
 */

// map映射和flatMap扁平化
object FlatMapDemo {
  def main(args: Array[String]): Unit = {

    println("------------flatMap---------------")
    val names = List("Alice", "Bob", "Nick")
    println(names.map(upper)) //结合中每个元素映射
    println(names.flatMap(upper)) //集合中所有元素，扁平化操作，scala中字符串也是集合

    println("------------flatMap会按照传入的函数参数递归下去---------------")
    val lines = List("hello scala for spark", "scala spark", "hello spark")
    println(lines.flatMap(upper _))

  }
  def upper(s:String):String = {
    s.toUpperCase
  }
}
