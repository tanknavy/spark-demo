package com.tanknavy.implicit_conversion

/**
 * Author: Alex Cheng 9/14/2020 12:11 PM
 */

object TestClass {
  def main(args: Array[String]): Unit = {
    val t1 = new T1
    //t1.add()
    val t2 = T1.apply()
    //t2.add()
  }
  
  class T1{
    def add(): Unit ={
      println("class method")
    }
  }
  
  object T1{ //伴生对象？
    def apply(): T1 = {
      println("object apply method..")
      new T1()
    }
  }
}
