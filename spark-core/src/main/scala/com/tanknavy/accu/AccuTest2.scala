package com.tanknavy.accu

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Author: Alex Cheng 7/6/2020 9:21 AM
 * Spark累加器
 */

object AccuTest2 {
  def main(args: Array[String]): Unit = {
    //val conf = new SparkConf().setAppName("WordCount")
    val conf = new SparkConf().setAppName("HbaseTest").setMaster("local[*]") //如果想local模式测试一下，不放到集群/yarn上面

    //创建SparkContext对象
    val sc = new SparkContext(conf) // 对象ctrl+alt+v(scala), alt+enter(java)， ctrl+p, alt+\ 显示参数列表

    //累加器需求
    //driver端发到exetutor执行，sum在各个结果计算，并没有返回sum, collect只是收集rdd

    //var sum =0;
    //val sum: LongAccumulator = sc.longAccumulator("sum") //driver端创建一个累加器,各个executor计算完返回到driver再merge
    //使用自定义累加器,使用时必须注册
    val sum = new AccuCustomize()
    sc.register(sum,"sum") //注册累加器

    val values: RDD[Int] = sc.parallelize(Array(1,2,3,4))

    val maped:RDD[(Int,Long)]= values.map(x => {
      //sum += x //加到sum
      sum.add(x)
      (x,System.currentTimeMillis()) //transformation算子中加上时间戳，后面两次action算子调用，可以发现这个rdd会被计算两次
    })

    maped.cache() //要么cache,要么放到action

    maped.collect().foreach(println)

    //注意：第二次foreach会导致maped这个rdd会被计算两次，累加器必须放到action算子中，防止transformation重复操作
    //所以，要么累加器要么放到action算子中，要么cache起来(cache后如果多次调用不会再计算rdd)
    maped.foreach(println)

    println("--------------------------------")
    //println(sum) //累加结果为0？这就是为啥
    println(sum.value) //累加结果为0？这就是为啥

    sc.stop()

  }
}
