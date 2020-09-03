package com.tanknavy.mysql

import java.sql.DriverManager

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Author: Alex Cheng 7/5/2020 6:55 PM
 */

object MySQLRDD {
  def main(args: Array[String]): Unit = {
    //val conf = new SparkConf().setAppName("WordCount")
    val conf = new SparkConf().setAppName("MySQLRDD").setMaster("local[*]") //如果想local模式测试一下，不放到集群/yarn上面

    //创建SparkContext对象
    val sc = new SparkContext(conf) // 对象ctrl+alt+v(scala), alt+enter(java)， ctrl+p, alt+\ 显示参数列表

    //定义mysql连接参数
    val driver ="com.mysql.jdbc.Driver"
    val url="jdbc:mysql://spark3:3306/classicmodels"
    val user="hive"
    val passwd="q1w2e3r4"


    //JdbcRDD
    new JdbcRDD[Int](sc,
      () =>{
        Class.forName(driver) // 加载类对象，Returns the Class object associated with the class or interface, 等于Class.forName(className, true, currentLoader)
        DriverManager.getConnection(url,user,passwd) //DriverManager attempts to select an appropriate driver from the set of registered JDBC drivers
      },
      "select orderNumber from orders where ? <=orderNumber and orderNumber <=?",
      10100, 10106,
      1,
      rs=>rs.getInt(1) //resultSet解析
    ).foreach(println) //collect到客户端

    sc.stop()


  }
}
