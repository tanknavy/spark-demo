package com.tanknavy.rdd

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

/**
 * Author: Alex Cheng 7/8/2020 1:35 PM
 */

object RDD2DStream {
  def main(args: Array[String]): Unit = {

    //创建SparkConf
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd2Dtram")

    //创建StreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(3))
    //val ssc2 = new StreamingContext(sparkConf, Milliseconds(300)) //minute, second, millisecond

    //测试时，可以从RDD队列中创建DStream
    //创建RDD队列
    val seqRdd = new mutable.Queue[RDD[Int]]

    //读取RDD队列创建DStream
    //val rddDStream: InputDStream[Int] = ssc.queueStream(seqRdd) //一次拿一个，结果一直是55
    val rddDStream: InputDStream[Int] = ssc.queueStream(seqRdd,false) //队列中有一个拿一个，根据延时可能不同结果55/110两个结果

    rddDStream.reduce(_+_).print()

    //启动
    ssc.start()

    //注意：这段代码必须放到awaitTermination前面
    //循环添加rdd到queue集合中
    for(i <- 1 to 50){
      seqRdd += ssc.sparkContext.makeRDD(1 to 10, 1)
      //seqRdd += ssc.sparkContext.parallelize(1 to 100, 10)
      Thread.sleep(2000)
    }

    ssc.awaitTermination()

  }
}
