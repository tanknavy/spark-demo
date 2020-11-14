package com.tanknavy.flow

/**
 * Author: Alex Cheng 11/13/2020 9:07 PM
 */

object YieldDemo {
  def main(args: Array[String]): Unit = {
    //1 将1 to 10 进行遍历
    //2.yield i 将每次循环得到的i放到集合vector中，并返回给res
    //3. i这里是一个代码块，意味可以对对i进行处理
    //4. 体现scala语法特点: 将集合中各个数据进行处理，并返回新的集合
    val res = for (i <- 1 to 10) yield {// 将集合中各个数据进行处理，并返回新的集合(Vector)
      if( i % 2 == 0){
        i
      }else{
        "不是偶数"
      }
    }
    println(res)
  }
}
