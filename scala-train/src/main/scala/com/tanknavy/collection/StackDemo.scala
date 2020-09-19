package com.tanknavy.collection

import scala.collection.mutable

/**
 * Author: Alex Cheng 9/18/2020 5:06 PM
 */

object StackDemo {
  def main(args: Array[String]): Unit = {
    //import scala.collection.immutable.Stack
    import scala.collection.mutable.Stack
    val stack = new mutable.Stack[String]()
    stack.push("aa")
    stack.push("bb")
    stack.push("cc")
    stack.push("dd")
    println(stack)
    stack.pop()
    println(stack.head)
    println(stack)
  }
}
