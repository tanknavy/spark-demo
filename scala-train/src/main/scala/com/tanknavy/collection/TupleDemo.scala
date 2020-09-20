package com.tanknavy.collection

/**
 * Author: Alex Cheng 9/18/2020 9:35 AM
 */

object TupleDemo {
  def main(args: Array[String]): Unit = {
    //为了将多个无关数据封装为一个整体，对数据没有过多的约束，使用()产生Tuple2,Map中每个元素就是Tuple2
    //为了高效操作元素，编译器根据元素的个数不同，对应Tuple1,Tuple2,...Tuple22
    val t1 = (1,2,3,"hello", 3.14,'A')
    println(t1)
    println(t1._5)
    println(t1.productElement(4))

    /* override def productElement(n: Int) = n match {
      case 0 => _1
      case 1 => _2
      case 2 => _3
      case 3 => _4
      case 4 => _5
      case 5 => _6
      case _ => throw new IndexOutOfBoundsException(n.toString())
    }*/

    //---tuple遍历---------
    //tuple是一个整体，遍历需要iterator，而不是传统for
    //for(item <- t1){println(item)) //错误
    for(item <- t1.productIterator){ //迭代器像一个移动的指针
      println("item= " + item)
    }


  }
}
