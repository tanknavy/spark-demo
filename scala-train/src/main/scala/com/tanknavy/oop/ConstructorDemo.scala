package com.tanknavy.oop

import scala.beans.BeanProperty

/**
 * Author: Alex Cheng 9/15/2020 10:47 AM
 */

object ConstructorDemo {
  def main(args: Array[String]): Unit = {
    val p1 = new Person("bob",28)
    val p2 = new Person("tom")
    println(p1, p2)

    println("---------主、辅助构造器细节-------------------")
    val b1 = new B //B的主构造器默认执行了父类的构造器，相当于隐式super
    val b2 = new B("b2")//B的辅助构造器中第一句必须调用柱构造器（this）

    println("--------类属性-------------------------")
    val w1 = new Worker("smith")
    println(w1.name)
    //w1.inName //错误，因为inName是局部变量
    val w2 = new Worker2("bob")
    println(w2.name)
    //w2.inName = "bob2" //错误，val修饰属性只读，没有提供_$eq赋值方法
    println(w2.inName)
    val w3 = new Worker3("tom")
    println(w3.name)
    w3.inName = "tom2"
    println(w3.inName)

    println("--------@BeanProperty产生类似setter/getter-------")
    val w4 = new Worker4
    w4.name = "adm"
    w4.getName //@BeanProperty自动生成
    w4.setName("jack")

    println("------------主构造函数私有化------")
    //val w5 = new Worker5 //class Worker5 private()
    val w5 = new Worker5("Bob")
    println(w5.name)


  }

  //Scala构造器, 主构造器和辅助构造器
  //形参和属性不能同名，因为属性没有this区分,所以一般前面加in
  class Person(inName:String, inAge:Int){
    var name: String = inName //参数值赋给属性
    var age: Int = inAge

    //主构造器会执行类定义中的所有语句(除去其它方法)！
    age += 10
    println("constructor output")

    def this(name: String){//辅助构造器,名称都是this
      //必须在第一行显示调用主构造器,没有会报错,why?实现调用主构造器中调用父类构造器super
      this("alex",10)
      this.name = name
    }

    //重写了toString方法
    override def toString: String = {
      "name=" + this.name + ",age= " + this.age
    }
  }
}

//测试为什么辅助构造器def this()中一定要调用主构造器this，
// 因为需要保持继承关系，同时辅助构造器不支持直接调用父类主构造器
//先父类构造器->主类构造器->辅助构造器
class A {
  println("A constructor executed") //主构造器会执行类中所有定义的语句(除掉其它方法)
  def methodA(): Unit ={
    println("A method")
  }
}

class B extends A{ //默认构造器可以不写(),extends A会导致子类先执行父类对应的构造器(无参、有参)
  var id: Int = _
  println("B main consturctor")//主构造器会执行类中所有定义的语句(除掉其它方法)

  def this(name:String){//辅助构造器
    //super() //辅助构造器不支持直接调用父类主构造器！但是可以调用其它方法
    this //必须先调用A的主构造器(直接或间接)，主构造器中又会执行父类的主构造器
    println("call B this(name:String)")
  }
}

//类属性的高级部分
//如果主构造器的形参没有修饰符，默认是局部变量
//如果使用val，Scala会将此参数作为类的私有的只读属性
//如果使用var，scala会将此参数作为类的成员属性，并提供属性对应的类似getter/setter方法，成员属性是私有，但是可读写
class Worker(inName:String){ //inName是局部变量
  var name:String = inName
  var age:Int = _
  println("worker main constructor")

  def this(inAge:Int){
    this("bob")
    this.age = inAge
  }
}

class Worker2(val inName:String){//inName属性私有，只读
  var name = inName
  println("worker2 main constructor")
}

class Worker3(var inName:String){//inName属性私有，可读写
  //生成类似java bean的setter/getter方法
  var name = inName
  println("worker3 main constructor")
}

class Worker4{ //为了和java bean setter/getter互操作，框架
  @BeanProperty var name: String = null//自动生成getter/setter
}


class Worker5 private (){//inName属性私有，可读写, 构造函数私有化
  var name = "adm"
  println("worker5 main constructor")

  def this(inName: String){
    this //必须先调用主构造器
    this.name = inName
    println("构造函数私有化,调用的辅助构造函数")
  }
}