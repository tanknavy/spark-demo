package com.tanknavy.oop_trait

/**
 * Author: Alex Cheng 9/24/2020 2:58 PM
 */

//动态混入，在不修改类的定义基础上可以使用trait方法，在对象的创建时混入新功能，扩展类的功能
//解耦，符合OCP原则，对修改关闭，对扩展开放,implicit隐式类和mixin都符合OCP原则
//python里面多继承也有mixin
object DynamicMixInDemo {
  def main(args: Array[String]): Unit = {
    //动态mixin, 没有extends trait，但是对象拥有它的功能
    val oracleDB = new OracleDB with Tr01 //不影响子类，动态混入新功能
    oracleDB.insert(98)

    //也可以给abstract动态扩展
    val mysql = new MySQL with Tr01 //抽象类不是不可以实例化吗？因为这个抽象类没有任何方法，所以new一下相当于实现了更全部方法，像匿名字类
    mysql.insert(200)

    //如果抽象类有抽象方法的动态混入特质
    val mysql2: MySQL2 with Tr01 = new MySQL2 with Tr01 { //抽象类把with放实现前面
      override def say(): Unit = {
        println("mysql2 say hi")
      }
    }
    mysql2.insert(299)
    mysql2.say()


  }
}

trait Tr01{
  def insert(id:Int):Unit = {
    println("此trait将被动态混入...")
    println("插入数据= " + id)
  }
}

class OracleDB{}

abstract class MySQL{}

abstract class MySQL2{
  def say()
}