package com.tanknavy.high_function

/**
 * Author: Alex Cheng 9/13/2020 11:00 AM
 */

object RecursiveMax {
  def main(args: Array[String]): Unit = {
    val list = List(1,2,3,4,5,-10,6,7,8,99)
    println(myMax(list))
  }

  //递归求List中最大值，利用List.head和List.tail
  //套路:新参是新的输入变量值，返回是最终想要的类型
  //真正纯函数不依赖外部变量保存状态
  //list = List(1,2,3),list.head返回1，list.trail返回(2,3)
  def myMax(list: List[Int]): Int = {
    if (list.isEmpty) //为空抛出异常
      throw new java.util.NoSuchElementException()
    else if (list.length == 1) //一个元素返回头
      return list.head
    else //头比剩余任何一个都大就返回head,否则递归
      //递归是告诉计算机做什么，而不是告诉计算机怎么多(迭代)
      // 最大值要么在head,要么在剩余的tail, 假设tail处理好了
      if (list.head > myMax(list.tail)) return list.head//最大值在头
      else return myMax(list.tail) //最大值在去处head的剩余tail
  }
}
