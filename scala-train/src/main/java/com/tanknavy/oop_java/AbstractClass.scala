package com.tanknavy.oop_java

/**
 * Author: Alex Cheng 9/17/2020 8:46 AM
 */

object AbstractClass {
  def main(args: Array[String]): Unit = {
    println("abstract class")
    //默认情况下，一个抽象类是不能实例化的，但是实例化是，动态实现抽象类的抽象方法也ok

    var animal = new Animal { //匿名子类，在代码块中实现抽象属性和方法
      override var name: String = _
      override var age: Int = _

      override def eat(): Unit = {
        println("love eating")}
    }
    animal.eat()

  }
}

//scala抽象类中，抽象属性，抽象方法
//抽象类的价值：更多使用在设计，让子类继承并实现抽象类（实现抽象类的抽象方法）
abstract class Animal{
  var name: String //抽象字段
  var age: Int //抽象字段
  var color: String = "black" //普通字段
  //abstract def eat() //抽象方法
  def eat() //抽象方法, 无需abstract关键之

  def sleep(): Unit ={ //抽象类中可以有抽象方法
    println("sleep in winter")
  }
}


class Cat extends Animal{//alt+enter实现抽象方法
  override var name: String = _
  override var age: Int = _

  override def eat(): Unit = {
    println("love fish")
  }
}
