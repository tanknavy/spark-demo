package com.tanknavy.implicit_conversion

/**
 * Author: Alex Cheng 9/13/2020 10:58 PM
 */

//隐式转换：隐式转换函数(给数据转换类型)，隐式类(给对象添加方法)
object ImplicitFunc {
  def main(args: Array[String]): Unit = {
    //隐式转换函数 Double =>Int转换, 隐式函数的匹配只能是唯一的
    implicit def d2i(d: Double): Int = { //编译器生成d2i$1这个函数
      d.toInt
    }

    //底层编译d2i$1(3.6)这样执行
    val num: Int = 3.6 //有下划线表示有隐式函数转换
    println(num.getClass, num.getClass.getSuperclass) //返回所属类对象

//   var obj =  num  match {
//    case a: String => println("string type")
//    case b: Double => println("double type")
//    case c: Int => println("double type")
//    case _ => println("other type")
//    }

  }
}
