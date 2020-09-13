package com.tanknavy.function

/**
 * Author: Alex Cheng 9/12/2020 9:49 AM
 */

object LazyLoad {
  def main(args: Array[String]): Unit = {
    //val res = sum(10, 20)
    lazy val res = sum(10, 20) //lazy函数在值res真正需要时才执行
    println("---------------------------------")
    println("res=" + res) //在需要使用res时才执行
  }

  def sum(n1: Int, n2: Int): Int = {
    println("sum()执行了")
    n1 + n2
  }
}
