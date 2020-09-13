package com.tanknavy.function

/**
 * Author: Alex Cheng 9/11/2020 8:28 PM
 */

//递归
object Recursive {

  def main(args: Array[String]): Unit = {
    func(4)
    func2(4)
  }

  def func(n:Int): Unit ={ //递归调用
     if (n>2){ //递归和终止条件
       func(n-1)
       //func(n+1) //StackOverflowError栈溢出，和下面错误不一样哦！！！
     }
     println("n=" + n) //每次递归最后都会执行
   }

  def func2(n:Int): Unit ={ //递归调用
    if (n>2){ //递归和终止条件
      func2(n-1)
      //n会无穷大直到最大值，这时再+1，符号位由正变负数满足else中退出条件，输出最大负数
      //func2(n+1)
    }else{
      println("n2=" + n) //最终退出条件才会执行
    }
  }
}
