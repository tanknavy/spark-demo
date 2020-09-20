package com.tanknavy.high_function

/**
 * Author: Alex Cheng 9/18/2020 9:05 PM
 */
//高阶函数定义
//函数 _下划线的含义

object HighOrderFuncDemo {
  def main(args: Array[String]): Unit = {
    //高阶函数
    val res = test(sum, 3.14)//函数作为参数时不要写()，写了表示执行了
    //如下使用 _表示将函数赋予鼻梁
    val res2 = test(sum _, 3.14)//函数作为参数时不要写(), 使用_表示函数不是执行结果
    println(res)
    println(res2)

    //函数作为变量的传递
    val f1 = myPrint//f1为Unit类型(即myPrint的返回类型)，会执行这个函数
    //f1: Unit //f1为Unit类型
    println(f1) //() 返回为空
    val f2 = myPrint _//后面带_不会执行这个函数，f2是函数类型
    //f2: ()=>Unit //f2为 ()=>Unit就是函数类型
    println(f2) //<function0>

    //高阶函数示例:无参函数传入
    test2(sayOk) //传入无参，无返回的函数作为参数
    //test2(sayHi) //传入的函数type mismatch
  }

  //高阶函数, 函数参数为函数，传入自定义函数 f:A=>B
  def test(f:Double =>Double, n1:Double) ={
    f(n1)
  }
  //普通函数
  def sum(d:Double):Double ={
    println("sum is called...")
    d + d
  }

  def myPrint()={
    println("function to variable")
  }

  //test2是高阶函数，可以接受没有输入，返回为Unit的函数
  def test2(f:()=>Unit) ={
    f()
  }

  def sayOk()={
    println("say ok!")
  }

  def sayHi(name:String) ={
    println("hello " + name)
  }

}
