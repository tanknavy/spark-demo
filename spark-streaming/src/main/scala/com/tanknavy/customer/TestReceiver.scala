package com.tanknavy.customer

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

/**
 * Author: Alex Cheng 7/8/2020 2:30 PM
 */

object TestReceiver {
  def main(args: Array[String]): Unit = {
    //创建SparkConf
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCountStreaming")

    //创建StreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(3))
    //val ssc2 = new StreamingContext(sparkConf, Milliseconds(300)) //minute, second, millisecond

    //spark抽象是rdd->dataframe/dataset->DStream
    //创建DStream，通过端口，nc -lk 7777
    //val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("spark3",7777)
    //使用自定的Receiver
    val lineDStream: ReceiverInputDStream[String] = ssc.receiverStream(new ReceiverCustomer("spark3",7777))

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
