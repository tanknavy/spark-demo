package com.tanknavy.udaf

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Author: Alex Cheng 7/7/2020 8:56 AM
 */

object TestUDAF {
  def main(args: Array[String]): Unit = {
    //Spark Session创建
    val spark: SparkSession = SparkSession
      .builder() //在伴生对象中
      .master("local[*]")
      .appName("sparksql")
      //.enableHiveSupport()
      .getOrCreate()

    //导入隐式转换 //这里spark不是包名，而是SparkSession的名字

    val df: DataFrame = spark.read.json("C:\\software\\spark-2.3.3-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")

    //创建临时表
    df.createTempView("person")

    //注册UDAF
    spark.udf.register("myAvg",CustomerUDAF)

    //使用UDAF
    spark.sql("select myAvg(age) from person").show

    //关闭连接
    spark.stop()
  }
}
