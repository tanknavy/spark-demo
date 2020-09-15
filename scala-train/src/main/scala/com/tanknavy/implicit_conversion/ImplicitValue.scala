package com.tanknavy.implicit_conversion

/**
 * Author: Alex Cheng 9/14/2020 11:04 AM
 */

//编译器的优先级为 传值>隐式值>默认值
object ImplicitValue {
  def main(args: Array[String]): Unit = {
    implicit val str: String = "Bob" //隐式值
    //implicit val str2: String = "Good" //多个同类型隐式值时编译器会报错

    sayHi//底层调用sayHi(str)，使用隐式值不要写()
    //sayHi()

    //name是函数隐式参数,
    // 隐式值优先级大于默认值，因为在编译器里面写了sayHi(str)
    def sayHi(implicit name: String ="Carl" ):Unit={
      println(name + " hello!")

      //scala里面函数里面可以定义函数，编译器会加$1,$2区别
      def sayHi():Unit ={
        println("no parameter " + "hello")
      }
    }



  }
}
