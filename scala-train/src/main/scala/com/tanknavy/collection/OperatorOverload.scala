package com.tanknavy.collection

/**
 * Author: Alex Cheng 9/18/2020 12:58 PM
 */

object OperatorOverload {
  def main(args: Array[String]): Unit = {
    val dog = new Dog
    dog + 3 //自定义操作符
    dog.+(2) //想函数一样使用
    println(dog.age)
  }
}

class Dog{
  var age:Int = 0
  def +(n:Int)= { //操作符重载
    this.age += n
  }
}
