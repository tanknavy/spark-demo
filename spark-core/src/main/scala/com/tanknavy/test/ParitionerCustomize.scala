package com.tanknavy.test

import org.apache.spark.Partitioner

/**
 * Author: Alex Cheng 7/5/2020 12:02 PM
 * Spark自定义分区器
 */

class ParitionerCustomize(paritioins: Int) extends Partitioner{

  //获取分区数量
  override def numPartitions: Int = paritioins

  //按照key获取分区号，这里简单的0分区
  override def getPartition(key: Any): Int = {
    0
  }
}
