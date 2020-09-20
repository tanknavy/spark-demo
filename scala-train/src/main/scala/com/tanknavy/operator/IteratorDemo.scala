package com.tanknavy.operator

/**
 * Author: Alex Cheng 9/19/2020 9:05 PM
 */

object IteratorDemo {
  def main(args: Array[String]): Unit = {
    val iterator = List(1,2,3,4,5).iterator //抽象类AbstractIterator的匿名子类
    val iterator2 = List(1,2,3,4,5).iterator //得到迭代器
    "-----while遍历-----------"
    while(iterator.hasNext){
      println(iterator.next())
    }


    "-----for遍历-----------"
    for(enum <- iterator2){
      println(enum)
    }
  }
}
