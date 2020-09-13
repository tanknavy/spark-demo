package com.tanknavy.high_function

/**
 * Author: Alex Cheng 9/12/2020 11:45 AM
 */

/*
为什么要使用partial function，以下需求
思路1：filter+map+map
思路2：match case
思路3：对于符合某个条件的情况进行逻辑操作，使用偏函数，使用一组case语句封装的函数

 */

object PartialFuncDemo {
  def main(args: Array[String]): Unit = {
    val list = List(1,2,3,4,5,3.14,"hello")
    //list.filter(f1).map(m1) //因为f1过后类型还是Any
    println(list)
    //println(list.filter(f1).map(m2).map(m1))
    //println(list.map(addOne))

    //思路三：定义一个偏函数
    var partialFun= new PartialFunction[Any,Int] { //偏函数接受Any类型，输出Int类型
      //override def isDefinedAt(x: Any): Boolean = if x.isInstanceOf[Int] true else false
      override def isDefinedAt(x: Any): Boolean = x.isInstanceOf[Int] //如果返回true，就会调用apply构建对象实例，如果false就过滤掉

      override def apply(v1: Any): Int = { //apply构造器，对传入的值+1并返回
        v1.asInstanceOf[Int] + 1 //前面判断是Int类型后，这里as转换为Int操作
      }
    }
    //使用偏函数，map不支持偏函数, 使用collet
    var list2 = list.collect(partialFun)
    println(list2)

    //偏函数两种简写方式一i
    //可以加一个隐式转换
    def partialFun2: PartialFunction[Any,Int] = {
      case i:Int => i + 1//如果是int类型
      case j:Double => (j*2).toInt//如果是int类型
    }
    var list3 = list.collect(partialFun2)
    println(list3)

    //偏函数两种简写方式二
    var list4 = list.collect{
      case i:Int => i+1
      case j:Double => (j*2).toInt
    } //collect相当于filter+map
    println(list4)


    //思路一：filter+map
    def f1(n: Any): Boolean ={ //Any是一切类的父类
      n.isInstanceOf[Int]
    }

    def m1(n: Int):Int={
      n+1
    }

    def m2(n:Any):Int={
      n.asInstanceOf[Int]
    }

    //思路2:模式匹配
    def addOne(n:Any): Any={
      n match {
        case x: Int => x +10//类型匹配
        case _  =>
      }
    }

  }
}
