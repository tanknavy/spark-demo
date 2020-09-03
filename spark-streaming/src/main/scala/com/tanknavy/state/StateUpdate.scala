package com.tanknavy.state

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

/**
 * Author: Alex Cheng 7/8/2020 12:15 PM
 * 有状态转换
 */

object StateUpdate {
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

    //按照key聚合,reduceByKey是无转态的，只做这一batch
    //val wordAndCount: DStream[(String, Int)] = wordAndOne.reduceByKey(_+_)
    //定义更新函数变量
    val updateFunc: (Seq[Int], Option[Int]) => Some[Int] = (values:Seq[Int], status:Option[Int]) =>{

      val sum:Int = values.sum //当前batch内容的计算结果,因为是byKey的，所以values是一个key下面全部value的集合，这里求和,就用sum
      //状态信息中上一次转态
      val lastStatus: Int = status.getOrElse(0) //历史累计batch

      Some(sum + lastStatus)//本次集合求和，加上上次的结果
    }

    //checkpoint地址,可以返现sparkSession, ssc都包含sparkContext
    ssc.sparkContext.setCheckpointDir("./checkpointdir")

    //有状态的全局更新
    val wordAndCount: DStream[(String, Int)] = wordAndOne.updateStateByKey(updateFunc)

    //触发计算的行动操作
    wordAndCount.print()

    //开启流处理，不是像core/sql的关闭
    ssc.start()

    //除了Driver程序，还有接收器Receiver线程
    ssc.awaitTermination() //等待所有线程退出主线程才终端

  }

  //更新函数
  def update(values:Seq[Int], status:Option[Int]) ={
    val sum:Int = values.sum //当前batch内容的计算结果
    //状态信息中上一次转态
    val lastStatus: Int = status.getOrElse(0) //历史累计batch

    Some(sum + lastStatus)
  }

}
