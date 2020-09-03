package com.tanknavy.test

import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
 * Author: Alex Cheng 7/6/2020 8:26 PM
 * json->datafram->dataset
 */

object TestSparkSQl {
  def main(args: Array[String]): Unit = {

    //Spark Session创建,
    val spark: SparkSession = SparkSession
      .builder() //在伴生对象中
      .master("local[*]")
      .appName("sparksql")
      //.config("spark.sql.hive.convertMetastoreParquet", false)
      .config("spark.submit.deployMode", "client")
      //.config("spark.jars.packages", "org.apache.spark:spark-avro_2.11:2.3.3")
      //.config("spark.sql.warehouse.dir", "/user/hive/warehouse")
      //.config("hive.metastore.uris", "thrift://hivemetastore:9083")
      //.enableHiveSupport() //spark-shell中默认支持hive,代码中要显式指定
      .getOrCreate()

    //conf使用kyro序列化，注册类
    //https://blog.knoldus.com/kryo-serialization-in-spark/
    spark.sparkContext.getConf.set("spark.kryo.registrationRequired", "true").registerKryoClasses(Array(classOf[Person],classOf[Array[Person]]))

    //导入隐式转换
    import spark.implicits._ //这里spark不是包名，而是SparkSession的名字

    //创建DataFrame
    //val df: DataFrame = spark.read.json("C:\\software\\spark-2.3.3-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")//使用中出错，换成如下format指定格式
    //使用dataframe格式
    //val df: DataFrame = spark.read.format("json") .option("header", "false").option("timestampFormat", "MM/dd/yyyy").load("C:\\software\\spark-2.3.3-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")
    //使用Dataset格式
    val df: Dataset[Row] = spark.read.format("json") .option("header", "false").option("timestampFormat", "MM/dd/yyyy").load("C:\\software\\spark-2.3.3-bin-hadoop2.7\\examples\\src\\main\\resources\\people.json")

    //DSL风格
    df.filter($"age">20).show()

    //SQL风格，需要创建临时表
    df.createOrReplaceTempView("person")
    spark.sql("select * from person where age >20").show

    //样例类,注意样例类必须在App外，否则会报以下编译错误
    // Unable to find encoder for type stored in a Dataset.
    // Primitive types (Int, String, etc) and Product types (case classes) are supported by importing spark.implicits.
    //case class Person(age:Long, name:String) extends Serializable

    //创建DataSet
    //需要tuple和case class之间转换的Encoder?
    import org.apache.spark.sql.{Encoder, Encoders}
    import spark.implicits._
    val ds: Dataset[Person] = df.as[Person]
    ds.show()

    //关闭连接
    spark.stop()
  }


}

//样例类,注意样例类必须在App外，否则会报以下错误
//通过反射的方式, 元祖和样例类的对应，按照名字
case class Person(age:Long, name:String) extends Serializable