package com.tanknavy.PatternMatch

/**
 * Author: Alex Cheng 9/19/2020 11:53 PM
 */

//scala比java的switch更强大
object SwitchDemo {
  def main(args: Array[String]): Unit = {
    val oper = '*'
    val n1 = 10
    val n2 = 5
    var res = 0
    //java使用switch，不需要break
    //从上到下
    //switch(oper){} //java的switch
    oper match{ //可以支持其它类型(不仅仅是字符)，字符串是另外一种匹配模式
      case '+' => res = n1 + n2 // =>类似java的:, 后面是执行的代码块
      case '-' => res = n1 - n2
      case '*' => res = n1 * n2
      case '/' => res = n1 / n2
      case 3.14 => println("match Pi")
      case _ =>   println("oper error") //都没执行成功，类似java的default
    }
    println("res=" + res)
  }
}
