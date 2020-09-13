package com.tanknavy.function

/**
 * Author: Alex Cheng 9/12/2020 10:36 AM
 */

object ExceptionDemo {
  def main(args: Array[String]): Unit = {
    try{
      val r = 10 / 0
    }catch {
      case ex: ArithmeticException => println("除数为零的算术异常")
      case ex: Exception => println("捕获异常")
    }finally {
      println("try catch后最终要执行的，不管成功与否，一般用于释放资源")
    }
    println("程序继续执行")
  }

}
