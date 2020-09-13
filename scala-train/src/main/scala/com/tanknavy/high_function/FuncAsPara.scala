package com.tanknavy.high_function

/**
 * Author: Alex Cheng 9/12/2020 4:07 PM
 */

object FuncAsPara {
  def main(args: Array[String]): Unit = {
    //高阶函数一：函数作为参数的函数, 如何定义函数作为参数： dataType => dataType
    def test(f1: Double => Double, f2: Double => Int, num: Double) = {
      f1(f2(num)) //函数里面传入函数
    }

    val res = test(f1,f2,10.0)
    println(res)
  }

  def f1(d: Double): Double ={
    d + d
  }

  def f2(d: Double): Int = {
    d + d
    d.toInt % 3
  }

}
