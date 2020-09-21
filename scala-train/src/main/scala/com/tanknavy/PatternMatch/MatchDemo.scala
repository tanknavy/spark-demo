package com.tanknavy.PatternMatch

/**
 * Author: Alex Cheng 9/19/2020 11:53 PM
 */

//scala比java的switch更强大
object MatchDemo {
  def main(args: Array[String]): Unit = {
    val oper = '*'
    val n1 = 10
    val n2 = 5
    var res = 0
    println("1.----------match case 字符匹配----------- ")
    //java使用switch，不需要break
    //从上到下
    //switch(oper){} //java的switch
    oper match{ //可以支持其它类型(不仅仅是字符)，字符串是另外一种匹配模式
      case '+' => res = n1 + n2 // =>类似java的:, 后面是执行的代码块
      case '-' => res = n1 - n2
      case '*' => res = n1 * n2
      case '/' => res = n1 / n2
      case 3.14 => println("match Pi")
      case _ =>   println("oper error") //匹配不到默认，类似java的default
    }
    println("res=" + res)

    println("2.----match case if 条件守卫--------------")
    for(ch <- "+-3!") { //对字符串遍历,ch是Char类型
      var sign = 0
      var digit = 0
      ch match{
        case '+' =>sign = 1
        case '-' =>sign = -1
          //如果case后面有条件守卫即if, 这时_不是表示默认匹配，而是不接受变量
          // case _ if如果多个条件成立都会执行
        case _ if ch.toString.equals("3") => digit =3 //这时的_忽略得到的ch值，
        case _ if ch >10 => println("ch > 10")
        case _ => sign = 2 //匹配不到的默认
      }
      println(ch + " " +sign +" " + digit)
    }

    println("3.-----------match case _ --------------")
    for(ch <- "+-3!"){
      var sign = 0
      var digit = 0
      ch match {
        case _ => digit=3 //case _写在最前面会怎么样？只执行这个default，多个case _后面的都不会生效
        case '+' => sign=1
        case '-' =>sign = -1
      }
      println(ch +" " + sign +" " + digit)
    }

    println("4.--------------match case variable 相当于这个无条件匹配了----------")
    val ch = 'Z'
    ch match{
      case '+' => println("ok")
      case mychar => println("ok~" + mychar) //赋值给变量mychar， 相当于无条件匹配了,后面case _不会执行了
      case _ => println("ok~~~~")
    }

    val res2 = ch match{ //match是一个表达式，可以有返回值，返回值是匹配到的语句
      case '+' => ch + " hello返回"
      case mychar => "ok~" + mychar //ch赋值给变量mychar， 相当于无条件匹配了
      case _ => println("ok~~~~")
    }
    println("match return: " + res2)


    println("5.----------match 类型匹配 case var:Type-------------------")
    //val obj = Array(1,2,3) //直接给obj赋值检查类型会报错，必须得类似if else
    val v = 4
    val obj = if(v == 1) 1
    else if (v==2) "2"
    else if (v==3) Map("aa" ->1)
    else if (v==4) Array(1,2,3)

    val res3 = obj match{// 根据obj的类型来匹配
      case a: Int => a
      case b: Map[String, Int] => "obj is string-int map collection"
      case c: Array[Int] => "obj is int array"
      case d: BigInt => Int.MaxValue
      case _ => "not any type above"
    }
    println("match return the type: " + res3)

  }
}
