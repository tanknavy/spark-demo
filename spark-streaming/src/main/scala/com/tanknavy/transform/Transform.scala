package com.tanknavy.transform

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

/**
 * Author: Alex Cheng 7/8/2020 11:51 AM
 * DStream操作转为对RDD的操作,rdd又可以转成dataframe/dataset，
 */

object Transform {
  def main(args: Array[String]): Unit = {

    //创建SparkConf
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCountStreaming")

    //创建StreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(3))
    //val ssc2 = new StreamingContext(sparkConf, Milliseconds(300)) //minute, second, millisecond

    //spark抽象是rdd->dataframe/dataset->DStream
    //创建DStream，通过端口，nc -lk 7777
    val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("spark3",7777)

    //transform操作:DStream转成了rdd操作, transform中操作的是rdd,对比DStream的flatMap,Map,reduceByKey
    val wordAndCountDStream: DStream[(String, Int)] = lineDStream.transform(rdd => {
      val words: RDD[String] = rdd.flatMap(_.split(" "))
      val wordAndOne: RDD[(String, Int)] = words.map((_, 1))
      val value: RDD[(String, Int)] = wordAndOne.reduceByKey(_ + _)

      value
    })
    wordAndCountDStream.print()


    //开启流处理，不是像core/sql的关闭
    ssc.start()

    //除了Driver程序，还有接收器Receiver线程
    ssc.awaitTermination() //等待所有线程退出主线程才终端
  }
}
