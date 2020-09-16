//package com.tanknavy.scala_package

/**
 * Author: Alex Cheng 9/15/2020 4:56 PM
 */

//scala中pacakge可以分开写，并且package中可以创建package,class,object/trait
//包名和类名可以不一致，编译时编译器会自动放入相对的包路径下面

//scala中子包可以直接使用父包中的内容，同名时采用就近原则

//在包中可以包含pacakge/class/object/trait, 但是不能包含variable, function，这是jvm的局限 ==>使用包对象的技术

package com.tanknavy.scala_package{
  package sub_pack{

    import com.tanknavy.scala_package.myPack.Customer

    class Person{
      val name = "Nick"
      println("Person class")
    }

    object TestPackage{
      def main(args: Array[String]): Unit = {
        val person = new Person

        import com.tanknavy.scala_package.myPack
        val customer = new Customer


      }
    }
  }

  //包对象
  //1.在包中可以包含pacakge/class/object/trait, 但是不能包含variable, function，这是jvm的局限 ==>使用包对象的技术
  //2.package object myPackage表示创建一个包对象，必须同名
  //3.每一个包都可以有一个包对象，切只有一个,类似Python的包中__init__文件中可以写一些变量，方法，其它模块都可以引用!!!
  //4.在包对象中定义的变量和方法，就可以在对应的包中使用

  //5.在底层这个包对象会生成两个类，package.class和package$.class
  package object myPack { //同名包的包对象
    var name_in_pacakge = "king"
    def sayHi(): Unit ={
      println("package object say hi")
    }
  }

  package myPack{ //创建包
    class Customer{
      var id: Int = _
      var name: String = _
      println("Customer call package object: " + name_in_pacakge) //同包范围内直接调用
      sayHi() //同包范围内直接调用
    }
  }

}