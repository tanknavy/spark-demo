package com.tanknavy.test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Author: Alex Cheng 7/4/2020 5:13 PM
 * 维度计算
 */

object Top3Ad {
  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("ad")

    val sc = new SparkContext(sparkConf)

    val lines: RDD[String] = sc.textFile("file")


    //省份加广告,最细的粒度(各个维度)
    val proAndAdToOne: RDD[((String, String), Int)] = lines.map(x => {
      val fields: Array[String] = x.split("")
      ((fields(1), fields(4)), 1) //((province,ad),1)
    })

    val proAndAdToCount: RDD[((String, String), Int)] = proAndAdToOne.reduceByKey(_+_) // ((province,ad), total)

    //维度转换，放大粒度为了按照省内排序 (province,(ad,total))
    val proToAdAndCount: RDD[(String, (String, Int))] = proAndAdToCount.map(x =>(x._1._1, (x._1._2,x._2)))

    //省内聚合, groupByKey聚合，（province,Iterable(ad, count)）
    val provinceToAdGroup: RDD[(String, Iterable[(String, Int)])] = proToAdAndCount.groupByKey()

    //scala内排序，排序同时取前三, 组内从Iterable转到List再sort,最后take取前3
    //takeOrdered能用吗？它用于rdd
    val top3: RDD[(String, List[(String, Int)])] = provinceToAdGroup.mapValues(x => {
      x.toList.sortWith((a, b) => a._2 > b._2).take(3) //sortWith(函数)，输入两个元素比较大小返回布尔值,>降序，<升序
    })

    //比较sortWith,sortBy,sorted
    provinceToAdGroup.mapValues(x=>x.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)) // sortBy(属性，)找到某个比较的栏位，(Ordering[DataType].reverse)降序

    //provinceToAdGroup.mapValues(x=>x.toList.sorted(Ordering.Int.reverse).take(3))//按照natural order， Seq.sorted(Ordering.DataType.reverse)


    //输出结果
    top3.collect().foreach(println)
    //关闭连接
    sc.stop()


  }
}
