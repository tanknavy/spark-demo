package com.tanknavy.scala_package

/**
 * Author: Alex Cheng 9/15/2020 7:38 PM
 */
//package com.tanknavy.scala_package {
package bag {

  object packageTest {
    def main(args: Array[String]): Unit = {
      println("")
    }
  }

  package object thePack {
    var objectName: String = "bob"

    def add(): Unit = {
      println("package object")
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