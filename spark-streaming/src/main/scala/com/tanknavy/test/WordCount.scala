package com.tanknavy.test

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Milliseconds, Seconds, StreamingContext}

/**
 * Author: Alex Cheng 7/7/2020 7:09 PM
 * DStream是个抽象类，
 *
 */

object WordCount {
  def main(args: Array[String]): Unit = {

    //创建SparkConf
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCountStreaming")

    //创建StreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(3))
    //val ssc2 = new StreamingContext(sparkConf, Milliseconds(300)) //minute, second, millisecond

    //spark抽象是rdd->dataframe/dataset->DStream
    //创建DStream，通过端口，nc -lk 7777
    val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("spark3",7777)

    //压扁
    val wordDStream: DStream[String] = lineDStream.flatMap(_.split(" "))

    //转换为元祖
    val wordAndOne: DStream[(String, Int)] = wordDStream.map((_,1))

    //按照key聚合
    val wordAndCount: DStream[(String, Int)] = wordAndOne.reduceByKey(_+_)

    //触发计算的行动操作
    wordAndCount.print()

    //开启流处理，不是像core/sql的关闭
    ssc.start()

    //除了Driver程序，还有接收器Receiver线程
    ssc.awaitTermination() //等待所有线程退出主线程才终端


  }
}
