package com.tanknavy.oop_trait

/**
 * Author: Alex Cheng 9/17/2020 11:57 AM
 */

//trait = interface + abstract class
object TraitDemo {
  def main(args: Array[String]): Unit = {
    val c = new C
    val f = new F
    c.getConnect()
    c.showInfo()
    f.getConnect()
    f.showInfo()
  }
}

object T1 extends Serializable{ //java中的接口可以当做scala的trait
  //trait Serializable extends Any with java.io.Serializable
}

//定义trait
//当一个trait有抽象方法和非抽象方法时，
//1.一个trait在底层对应两个类， 一个是Trait01.class接口
//2.还对应Trait01$.class.class抽象类
//3.子类实现接口方法，调用抽象类中普通方法
trait Trait01{
  //定义一个规范
  var name:String//类似abstract class中抽象属性
  def getConnect()//抽象方法
  def showInfo(): Unit ={ //trait中可以有普通方法
    println("trait01 normal method...")
  }
}
//6个类继承关系
class A{}
class B extends A{}
class C extends A with Trait01{ //继承父类，再继承特征
  override var name: String = "mysql"
  override def getConnect(): Unit = {
    println("connect mysql...")
  }
}

class D{}
class E extends D{}
class F extends D with Trait01{ //继承父类，再继承特征
  override var name: String = "redshift"
  override def getConnect(): Unit = {
    println("connect redshift")
  }
}
