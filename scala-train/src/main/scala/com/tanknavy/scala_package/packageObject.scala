package com.tanknavy.scala_package

/**
 * Author: Alex Cheng 9/15/2020 7:38 PM
 */

//包对象
//package com.tanknavy.scala_package {
package bag {

  object packageTest {
    def main(args: Array[String]): Unit = {
      println("packageTest")
      thePack.add() //同一文件夹下的包对象thePack

      import com.tanknavy.scala_package.myPack._ //另外一个文件件下
      val cus = new Customer()
      println(cus.id)
      println(cus.name)
      sayHi()

    }
  }

  //包对象
  //1.在包中可以包含pacakge/class/object/trait, 但是不能包含variable, function，这是jvm的局限 ==>使用包对象的技术
  //2.package object myPackage表示创建一个包对象，必须同名
  //3.每一个包都可以有一个包对象，切只有一个,类似Python的包中__init__文件中可以写一些变量，方法，其它模块都可以引用!!!
  //4.在包对象中定义的变量和方法，就可以在对应的包中使用
  package object thePack {
    var objectName: String = "bob"

    def add(): Unit = {
      println("package object thePack")
    }
  }

  package thePack {

    class Student {
      var name: String = ""
      var id: Int = 0
      println("Student class" + objectName)
    }

  }


}