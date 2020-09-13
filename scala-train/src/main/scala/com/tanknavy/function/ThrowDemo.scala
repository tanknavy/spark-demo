package com.tanknavy.function

/**
 * Author: Alex Cheng 9/12/2020 10:47 AM
 */

object ThrowDemo {
  def main(args: Array[String]): Unit = {
    val res = test()
    //println(res.toString)
    println("会执行吗")
  }

  def test(): Nothing ={ //Nothing是所有类的子类
    throw new Exception("异常出现")
  }
}
