package com.tanknavy.high_function

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Author: Alex Cheng 9/13/2020 9:40 AM
 */

//Scala函数式编程推荐使用Recursive代替while等loop循环
object RecursiveSum {
  def main(args: Array[String]): Unit = {
    //传统loop
    whileLoop()//10s左右

    //loop改递归, 闭包，里面函数能使用外部变量
    var res = BigInt(0)
    var num = BigInt(1)
    val maxVal = BigInt(99999999l)
//    var begin: Date = new Date()
//    //loop2Recursive()
//    var end: Date = new Date()
//    println("total runtime in recursive: " +  (end.getTime - begin.getTime)/1000.00 + "seconds")

    println("---------------------")
    var res2 = BigInt(0)
    var num2 = BigInt(1)
    var begin2: Date = new Date()
    SumRecursive(num2, res2) //10s左右
    var end2: Date = new Date()
    println("total runtime in recursive: " +  (end2.getTime - begin2.getTime)/1000.00 + "seconds")

    //这个太ugly，里面的局部变量必须先在外面存在，不符合纯函数特点！！！
    def loop2Recursive():Unit={//scala中形参是val不可变
      if (num <= maxVal){
        res += num
        num += 1
        loop2Recursive()
      }else{
        println("result: " + res)
      }
    }
  }

  //推荐：这种递归函数才是真正纯函数, 返回类型是最终想要结果的类型
  def SumRecursive(num:BigInt, sum: BigInt):BigInt ={//不使用外部变量
    if(num <=99999999l){
      //巧妙，num不可变，传入+1不就可以了吗
      return SumRecursive(num + 1, sum + num)//即使传入num/sum是val类型
    }else return sum
  }

  def whileLoop(): Unit ={
    var begin: Date = new Date()
    var dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val beginTime = dateFormat.format(begin)
    println("begin=" + beginTime)

    var res = BigInt(0)
    var num = BigInt(1)
    var maxVal = BigInt(99999999l)

    while(num <= maxVal){//耗时11s
      res += num
      num += 1
    }
    println("res=" + res)

    var end: Date = new Date()
    val endTime = dateFormat.format(end)
    //println(end)
    println("total runtime in while: " +  (end.getTime - begin.getTime)/1000.00 + "seconds")
  }

}
