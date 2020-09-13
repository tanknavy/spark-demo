package com.tanknavy.high_function

import java.util.Date

/**
 * Author: Alex Cheng 9/13/2020 12:19 PM
 */
//斐波那契数，使用递归，这里因为是两个递归
object RecursiveFbn {
  def main(args: Array[String]): Unit = {
    var count = BigInt(0) //测试递归调用测试
    var count2 = BigInt(0)
    var count3 = BigInt(0)
    var count4 = BigInt(0)
    val n = 8

    //var s1 = new Date()
    println(fbn(n)) //递归循环了109次,指数次增长！！！
    println(fbn2(n)) //
    //var e1 = new Date()
    println(fbnLoop(n)) //普通循环了8次
    println(fbnLoop2(n))
    //val e2 = new Date()
    println("recursive count: " + count)
    println("loop count: " + count2)
    println("loop count: " + count3)
    println("loop count: " + count4)
    //println("count:" + count + "runtime: " + (e1.getTime - s1.getTime))
    //println("count2:" + count2 + "runtime: " + (e2.getTime - e1.getTime))

    //斐波拉契数列使用递归会指数级增长！！！非常恐怖
    def fbn(n: BigInt): BigInt ={
      count += 1 //计数
      if (n==1 || n==2) 1
      else fbn(n-1) + fbn(n-2)//两个递归，指数级增长
    }

    def fbn2(n:Int):Int= {
      var a=0; var b=1; //设计好的起始值
      //for (int i: list) //java的for-each loop
      for (i <- 0 until n){ //scala的for-each loop
        count4 += 1
        //a,b = b,a+b //scala,java不支持解构赋值！！
        var tmp = a;//需要中间变量
        a = b;
        b = tmp + b
      }
      a
    }

    def fbnLoop2(n: Int):Int ={
      if (n == 1 || n == 2) return 1
      var a,b =1
      var i = 3
      var res = 0
      while(i <= n){
        count3 += 1 //计数
        res = a + b
        a = b
        b = res
        i += 1
      }
      res
    }

    def fbnLoop(n: BigInt) :BigInt = {
      if (n == 1 || n == 2) return 1
      import scala.collection.mutable.ListBuffer
      var list = ListBuffer[BigInt](BigInt(1), BigInt(1))
      var i = 3
      while (i <= n) {
        count2 += 1 //计数
        list.append(list(i-3) + list(i-2))
        i += 1
        //println(list)
      }
      println(list)
      return list.last
    }

  }


}
