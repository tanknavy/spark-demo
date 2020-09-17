package com.tanknavy.oop

/**
 * Author: Alex Cheng 9/16/2020 10:56 AM
 */
//scala中属性都是通过方法去做，更严谨的方式，
//继承中属性、方法的访问级别,public, protected, private

object MyExtends {
  def main(args: Array[String]): Unit = {
    val student = new Student
    student.name = "Tom"
    student.showInfo()
    student.study()

    println("---------------------")
    val sub = new Sub
    sub.test100() //父类公有方法可直接调用
    //父类的protected在同包中不能访问，只能在子类中！编译器控制的,不让访问，虽然在编译后又转成了public
    //但是java中protected子类可以使用，同一个包下也可以
    //sub.test200() //父类的protected在同包中不能访问，只能在子类中！
    //sub.test300() //父类private方法不能访问
  }
}

//scala的继承，属性底层都是private的，会自动生成类似public的setter/getter方法
// 属性实际都是通过方法去访问的
class Person{
  var name: String = _
  var age: Int = _
  def showInfo()={
    println("Person name:" + this.name)
  }
}

class Student extends Person {
  override def showInfo(): Unit = {
    println("Student name:" + this.name)
  }
  def study()={
    println(this.name + " study spark...") //继承后直接使用
  }
}

//基类
class Base{
  var n1:Int = 1 //默认
  protected var n2:Int = 2 //子类可以访问
  private var n3:Int = 3 //私有

  def test100() = { //默认public方法
    println("base 100")
  }

  protected def test200()={ //子类可以调用
    println("base 200")
  }

  private def test300()= { //子类不可以调用
    println("base 300")
  }

}

class Sub extends Base{
  def sayOk() = {
    this.n1 = 10 //访问的本质是this.n1()方法
    this.n2 = 20 //父类proteted属性可以访问
    //this.n3 = 30 //错误，父类private属性不能访问
    println("子类访问父类" + this.n1 + this.n2)

    this.test100()
    this.test200() //父类protected方法可以在子类中调用
    //this.test300() //父类private方法子类不可以调用

  }
}