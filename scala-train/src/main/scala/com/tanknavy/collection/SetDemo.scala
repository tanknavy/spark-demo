package com.tanknavy.collection

/**
 * Author: Alex Cheng 9/18/2020 4:46 PM
 */

object SetDemo {
  def main(args: Array[String]): Unit = {
    val s01 = Set(1,2,3.14,"abc",'Z')//默认是不可变Set
    println(s01)
    //s01.add("cc") //不可变

    import scala.collection.mutable //可变
    val s02: mutable.Set[Any] = mutable.Set(1,2,3.14,"abc",'Z')
    val s03 = mutable.Set[String]()
    s03.add("aa")
    s03.add("bb")
    s03.add("cc")
    s03 += "dd"
    s03.+=("ee") // +=符号重载当做函数使用
    println(s03)

    //set的删除
    s03 -= "cc"
    s03.remove("dd")
    println(s03)

    //set的遍历，和普通的一样
    for(s <- s03) println(s)

  }
}
