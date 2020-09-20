package com.tanknavy.operator

/**
 * Author: Alex Cheng 9/19/2020 10:31 PM
 */

//对其它集合应用vie方法可以得到类似stream懒加载的特性，
// view方法总是产生一个被懒执行的集合
// view不会缓存数据，每次都要重新计算，比如遍历时才真正计算
// 程序中，对集合操作并不希望立即执行，而是在使用结果时才执行，可以使用view进行优化
object ViewDemo {
  def main(args: Array[String]): Unit = {
    //需求：
    def eq(i:Int):Boolean ={
      println("eq执行...")
      i.toString.equals(i.toString.reverse) //数字转成字符串，使用字符串的reverse方法,比如23 ->32
    }

    def multi(num:Int):Int ={
      //num * num
      num
    }

    println("--------没有使用view----------")
    //val vs1 = (1 to 100).map(multi).filter(eq) //Vector(1, 4, 9, 121, 484, 676)
    //println(vs1)


    println("--------使用view返回懒加载集合，lazy执行，用时才展开-------------------")
    //返回一个懒加载的集合SeqViewMF(...)
    val vs2 = (1 to 100).view.map(multi _).filter(eq _) //SeqViewMF(...),后面这些函数都没有执行
    println(vs2)
    println(vs2.head)
    println("-----遍历view----")
    for(item <- vs2){ //用的时候才真正执行，这时filter等函数才被执行
      println(item)
    }

  }
}
