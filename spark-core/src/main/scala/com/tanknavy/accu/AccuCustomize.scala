package com.tanknavy.accu

import org.apache.spark.util.AccumulatorV2

/**
 * Author: Alex Cheng 7/6/2020 10:59 AM
 * 自定义累加器类，一个sum求和的自定义累加器
 *
 * 广播变量是一个只读的，如果没有driver回将变量发给每个task一个，现在只需发给executor，广播变量不易太大，过大上G的话可以考虑放到redis里面
 * 在机器学习中特征向量
 */

class AccuCustomize() extends AccumulatorV2[Int,Int]{ //累加器[输入，输出]，可以自定其它自定义类型，比如集合Set

  //定义一个属性,或者放到构造函数里面
  var sum:Int =0

  override def isZero: Boolean = {sum==0}

  override def copy(): AccumulatorV2[Int, Int] = {
    val accu = new AccuCustomize() //复制一个对象
    accu.sum = this.sum
    accu
  }

  override def reset(): Unit = sum = 0


  override def add(v: Int): Unit = sum += v //executor端

  //driver端合并executor端传回来的数据
  override def merge(other: AccumulatorV2[Int, Int]): Unit = {
    this.sum += other.value //driver端合并
  }

  override def value: Int = sum
}
