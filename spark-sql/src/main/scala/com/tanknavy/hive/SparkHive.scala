package com.tanknavy.hive

import org.apache.spark.sql.SparkSession

/**
 * Author: Alex Cheng 7/7/2020 11:54 AM
 * hive的相关配置，要么复制core-site.xml,hdfs-site.xml, hive-site.xml三个配置文件当道resources下面，
 * 要么config("hive.metastore.uris", "thrift://spark3:10000")
 *
 */

object SparkHive {
  def main(args: Array[String]): Unit = {
    //Spark Session创建,
    val spark: SparkSession = SparkSession
      .builder() //在伴生对象中
      .master("local[*]")
      .appName("sparksql")
      .config("spark.sql.hive.convertMetastoreParquet", false)
      .config("spark.submit.deployMode", "client")
      .config("spark.jars.packages", "org.apache.spark:spark-avro_2.11:2.3.3")
      //.config("spark.sql.warehouse.dir", "/user/hive/warehouse")
      //.config("hive.metastore.uris", "thrift://hivemetastore:9083") //默认使用内置的，
      //.config("hive.metastore.uris", "thrift://spark3:10000")
      .enableHiveSupport() //spark-shell中默认支持hive,代码中要显式指定, core-site.xml,hdfs-site.xml, hive-site.xml三个配置文件当道resources下面，也可以config指定
      .getOrCreate()

    //HIVE_STATS_JDBC_TIMEOUT, hive metastore2.0以上版本时，spark读取metastore会报错
    spark.sql("show tables").show() //使用hql

    spark.stop()
  }
}
