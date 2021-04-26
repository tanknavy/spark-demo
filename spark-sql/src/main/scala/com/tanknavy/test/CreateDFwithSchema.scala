package com.tanknavy.test

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
 * Author: Alex Cheng 7/6/2020 10:19 PM
 */

object CreateDFwithSchema {
  def main(args: Array[String]): Unit = {

    //Spark Session创建
    val spark: SparkSession = SparkSession
      .builder() //在伴生对象中
      .master("local[*]")
      .appName("CreateDataFrameWithSchema")
      //.enableHiveSupport()
      .getOrCreate()

    //导入隐式转换
    import spark.implicits._ //这里spark不是包名，而是SparkSession的名字

    //https://sparkbyexamples.com/apache-spark-rdd/convert-spark-rdd-to-dataframe-dataset/
    //创建DataFrame的第三种方式, 从RDD获得，createDataFrame(rdd, schema)
    //创建RDD
    val rdd: RDD[String] = spark.sparkContext.textFile("C:\\software\\spark-2.3.3-bin-hadoop2.7\\examples\\src\\main\\resources\\people.txt")

    rdd.collect().foreach(println)

    //DataFrame的是RDD[Row]类型
    //每行记录如何分割？
    //val rddRow: RDD[Row] = rdd.map(x=>Row(x)) //RDD[Row]
    //val rddRow: RDD[Row] = rdd.map(_.split(",")).map(line=>Row(line(0),line(1).trim)) //RDD[Row],Row中是个二元元祖
    val rddRow: RDD[Row] = rdd.map(_.split(",")).map(line=>Row(line(0),line(1).trim.toInt)) //RDD[Row],rdd中是个元素转成Row,类型转换

    //创建schema:StructType，StructType，StructField都是case class
    //a::b::Nil组合成一个Seq集合的写法
    //注意：字段中有个空格如果处理？防止"".split数组越界
    //都采用StringType格式？
    //val structType = StructType(StructField("name",StringType)::StructField("age",StringType,nullable = true)::Nil)
    val structType = StructType(StructField("name",StringType)::StructField("age",IntegerType,nullable = true)::Nil) //类型转换

    //创建DataFrame
    //import spark.implicits._
    val columnName = Seq("name", "age")
    //val df1 = spark.createDataFrame(rdd).toDF(columnName:_*) // _*将array炸开成,分隔
    val ds1: Dataset[String] = spark.createDataset(rdd)
    val df: DataFrame = spark.createDataFrame(rddRow, structType)

    //显示数据
    df.show()

    spark.stop()

  }
}

//样例类,注意样例类必须在App外，否则会报以下错误
//case class Person(age:Long, name:String) extends Serializable