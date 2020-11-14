package com.tanknavy.flow

/**
 * Author: Alex Cheng 11/13/2020 9:54 PM
 */

//scala推荐使用for，while和do while都支持, golang中都是用for，range
object WhileLoop {
  def main(args: Array[String]): Unit = {
    var i =0; //变量，因为while使用了外部var类型变量i, 而for
    while(i < 5){ //先判断再执行，整个while语句的结果是Unit类型的()
      println("i=" + i)
      i += 1
    }
  }
}
