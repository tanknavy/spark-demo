package com.tanknavy.hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Author: Alex Cheng 7/6/2020 8:09 AM
 */

object HbaseTest {
  def main(args: Array[String]): Unit = {
    //val conf = new SparkConf().setAppName("WordCount")
    val conf = new SparkConf().setAppName("HbaseTest").setMaster("local[*]") //如果想local模式测试一下，不放到集群/yarn上面

    //创建SparkContext对象
    val sc = new SparkContext(conf) // 对象ctrl+alt+v(scala), alt+enter(java)， ctrl+p, alt+\ 显示参数列表

    //创建Hbase的Configure
    val configuration: Configuration = HBaseConfiguration.create()
    conf.set("hbse.zookeeper.quorum", "spark1,spark3")
    //https://stackoverflow.com/questions/53579016/tableinputformat-is-not-a-member-of-package-org-apache-hadoop-hbase-mapreduce
    conf.set(TableInputFormat.INPUT_TABLE,"rddtable") //在2.x中没有TableInputFormat,需要引入hbase-mapreduce包

    //读取Hbase数据创建RDD
    //val hbaseRDD: RDD[(Nothing, Nothing)] = sc.newAPIHadoopRDD(conf,//
    val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(configuration,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable], //rowkey
      classOf[Result]
    )

    //打印hbaseRDD的row
    hbaseRDD.foreach(x=>{
      x._2.rawCells().foreach(y=>println(Bytes.toString(CellUtil.cloneRow(y))))
    })

    //close
    sc.stop()

  }
}
