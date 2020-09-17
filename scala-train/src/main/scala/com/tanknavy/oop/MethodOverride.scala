package com.tanknavy.oop

/**
 * Author: Alex Cheng 9/16/2020 12:31 PM
 */

object MethodOverride {
  def main(args: Array[String]): Unit = {
    val employee = new Employee
    employee.name = "bob"
    employee.showInfo()

    println("-----------------")
    println(employee.isInstanceOf[Person])//emp是不是Person的实例
    println(classOf[Employee])
    println(employee.getClass.getSimpleName)

  }
}

//class Person {
//  var name: String = "bob"
//
//  def showInfo(): Unit = {
//    println("Person showInfo() " + name)
//  }
//}

class Employee extends Person {

  override def showInfo(): Unit = { //子类重写父类方法
    println("Employee showInfo() " + name)
    //不加super就是本类中方法，就递归死机了
    super.showInfo() //和java一样，子类调用父类已经被override的方法使用super
  }

  def work(): Unit ={
    println("work harder, create your company")
  }

}
