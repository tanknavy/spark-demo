package com.tanknavy.udaf

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

/**
 * Author: Alex Cheng 7/7/2020 8:29 AM
 * SparkSQL自定义聚集函数
 */

object CustomerUDAF extends UserDefinedAggregateFunction{

  //输入数据的类型
  override def inputSchema: StructType = StructType(StructField("input", LongType)::Nil)

  //缓存数据的类型
  override def bufferSchema: StructType = StructType(StructField("sum",LongType)::StructField("count",LongType)::Nil)

  //输出数据的类型
  override def dataType: DataType = DoubleType

  //函数稳定性的问题
  override def deterministic: Boolean = true


  //缓存数据的初始化
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = 0L //报Integer类型不能转换为Long,所以使用0L,整个过程数据类型要求完全一致！！！
    buffer(1) = 0L //0不能自动转为Long吗？？？
  }

  //分区内合并数据
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    //空值处理
    if(!input.isNullAt(0)){//如果输入的数值为空
      buffer(0) = buffer.getLong(0) + input.getLong(0)
      buffer(1) = buffer.getLong(1) + 1L
    }
  }

  //分区间合并数据
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0) //赋值buffer(0)，取值buffer.getLong(0)
    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  //计算最终结果
  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0).toDouble/buffer.getLong(1)
  }

}
