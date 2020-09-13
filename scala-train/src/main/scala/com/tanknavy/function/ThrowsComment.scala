package com.tanknavy.function

/**
 * Author: Alex Cheng 9/12/2020 10:59 AM
 */

object ThrowsComment {
  def main(args: Array[String]): Unit = {
    f01()
  }

  //java中指明函数会抛出何种异常给caller, caller可以自己处理也可以继续往上抛出
  //scala中声明此函数会抛出异常的类型
  @throws(classOf[NumberFormatException]) //NumberFormatException.class
  def f01()={
    "abc".toInt
  }
}


