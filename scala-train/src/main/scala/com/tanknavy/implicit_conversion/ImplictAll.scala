package com.tanknavy.implicit_conversion

/**
 * Author: Alex Cheng 4/25/2021 2:38 PM
 */

//implicit一般3种用途，implicit conversion隐式转换, implicit parameter隐式参数使用参数对象的方法或者属性, 给类增加method
object ImplictAll {
  case class Text(content: String)
  case class Prefix(text: String)

  //1)隐式值 implicit val在需要时，自动传入
  implicit val delimiter: String = ",, "

  //2)隐式函数 implicit def 实现隐式转换
  //3)隐式参数 implicit prefix在函数的arguments中, 隐式参数必须在后面
  implicit def string2Text(content: String)(implicit prefix: Prefix, separator: String) = { //隐式转换+隐式参数，使用对象的属性或者方法
    Text(prefix.text + separator + content)
  }

  def printText(text: Text) = {
    println(text.content)
  }

  def main(args: Array[String]): Unit = {
    printText("world!") //参数类型不匹配，需要隐式转换
  }

  implicit val prefixLOL = Prefix("hello") //隐式参数implicit prefix对象

  //4)隐式类 implicit class给一个类型增加新的方法, 类型作为隐式类构造函数的入参，所以隐式类构造函数只能有一个参数
}
