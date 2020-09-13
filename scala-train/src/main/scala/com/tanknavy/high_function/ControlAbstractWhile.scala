package com.tanknavy.high_function

/**
 * Author: Alex Cheng 9/13/2020 1:13 AM
 */
//函数curry化，控制抽象，递归实现while

object ControlAbstractWhile {
  def main(args: Array[String]): Unit = {
    //方法一：普通while循环
    var x = 5 //初始化
    while (x >0){ //条件
      x -= 1 //变化
      println("x=" + x)
    }
    println("-------------------------")

    //方法二：循环改递归
    loop2recursive(5)
    println("-------------------------")

    //方法三：currey科里化,控制抽象，递归
    x = 5 //要处理的数字是外部var变量
    //函数curry化实现，控制抽象，递归实现while
    until(x > 0){ //输入一个条件函数，再加具体实现代码块
      x -= 1
      println("x=" + x)
    }
  }

  //使用control abstract和函数curry化实现，递归实现while
  def until(condition: => Boolean)(block: => Unit) : Unit = {
    //类似while循环，使用递归！
    if (condition){ //条件
      block //逻辑
      until(condition)(block) //递归，并且里面函数看见了外部面看
    }
  }

  //直接loop转递归，x是val修饰不能改, 不得已内部搞个临时变量xx
  def loop2recursive(x :Int): Unit ={
    var xx = x
    if (xx >0){
      xx -= 1
      println("x=" + xx)
      loop2recursive(xx)
    }
  }


}
