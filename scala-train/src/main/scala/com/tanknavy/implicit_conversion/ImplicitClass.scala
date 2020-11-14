package com.tanknavy.implicit_conversion

/**
 * Author: Alex Cheng 9/14/2020 11:39 AM
 */

object ImplicitClass {
  def main(args: Array[String]): Unit = {

    //隐式类，在作用域范围呢，当创建MySQL实例时就会生效
    //编译器中 DB(mysql).addSuffix()
    implicit class DB2(val m: MySQL){ //主构造器必须只有一个形参, 给类增加新方法而不改变类的结构
      def addSuffix(): String ={
        m.getClass.getSimpleName + "scala"
      }
    }

    val mysql = new MySQL
    mysql.add
    //编译器把mysql实例传入DB2产生对象再调用方法
    println(mysql.addSuffix())//编译器DB2(mysql).addSuffix()

//    object DB2{
//      def apply(m: MySQL): DB2 = new DB2(m) {
//        println("create implicit class")
//      }
//    }

  }

  class MySQL(){
    def add(): Unit ={
      println("MySQL class add method")
    }
  }
}
