package com.tanknavy.scala_package

/**
 * Author: Alex Cheng 9/15/2020 9:04 PM
 */

object packageVisible {
  def main(args: Array[String]): Unit = {
    val clerk = new Clerk
    clerk.showInfo()
    //clerk.sal//私有属性不可访问
    Clerk.test(clerk)

    val person = new Person
    person.name //是有属性不可访问，但是可以设定包访问级别
  }
}

//伴生类和伴生对象，因为scala将static拿掉了，因为不是面向对象的原因
//但是运行在jvm中，设计了伴生类/伴生对象
//非静态属性和方法放到伴生类中，静态的内容放到伴生对象中
//类，伴生类，同时出现时
class Clerk{
  var name: String = "jack" //底层都是private, 可读可写
  private var sal:Double = 2999.00 //私有属性，只能读！！！

  def showInfo()= {
    println("name " + name + " sal=" +sal)//
  }
}

//伴生对象，就是写静态内容的位置
object Clerk{
  def test(c:Clerk) ={
    println("test() name=" + c.name + " sal=" + c.sal) //伴生对象中可以访问私有属性
  }
}

class Person{
  //私有属性，增加包访问权限, 依然是私有的但是扩大了访问范围
  private[scala_package] val name = "jack"
}

//scala包的导入
class Dog{ //scala中自动导入scala, predef
  import scala.beans.BeanProperty //导包作用域在代码块内
  import scala.collection.mutable.{HashMap,HashSet} //引进两个包
  import java.util.{HashMap=>JavaHashMap, List} //导包时可以临时重命名
  //导入util下面除去HashMap的全部，第一个_表示隐藏，第二个_表示引入全部
  import java.util.{HashMap =>_, _}
  @BeanProperty var name:String = "jack"
}

class Cat{
  //@BeanProperty var name:String = "" //需要导包
}