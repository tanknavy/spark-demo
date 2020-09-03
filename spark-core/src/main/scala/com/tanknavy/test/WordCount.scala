package com.tanknavy.test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Author: Alex Cheng 7/2/2020 10:50 PM
 */

object WordCount {
  def main(args: Array[String]): Unit = {
    //创建SparkConf

    val conf = new SparkConf().setAppName("WordCount")
    //val conf2 = new SparkConf().setAppName("WordCount").setMaster("local[*]") //如果想local模式测试一下，不放到集群/yarn上面

    //创建SparkContext对象
    val sc = new SparkContext(conf) // 对象ctrl+alt+v(scala), alt+enter(java)， ctrl+p, alt+\ 显示参数列表

    //读取文件
    val lines: RDD[String] = sc.textFile(args(0))
    val words: RDD[String] = lines.flatMap(_.split(" "))
    val wordAndCount: RDD[(String, Int)] = words.map((_,1)).reduceByKey(_+_)

    //输出保存到文件中
    wordAndCount.saveAsTextFile(args(1))

    //关闭连接
    sc.stop()

  }
}
