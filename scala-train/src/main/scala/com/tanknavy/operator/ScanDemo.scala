package com.tanknavy.operator

/**
 * Author: Alex Cheng 9/19/2020 12:09 PM
 */
//scan和fold做一样的操作，但是会把初始值，产生的中间结果保存到一个Vector类型集合
object ScanDemo {
  def main(args: Array[String]): Unit = {

    println("-------scan扫描保存初始值和全部中间结果，是Vector类型-----------")
    val list09 = (1 to 5).scanLeft(5)(_ - _) //1 to 5是属于indexSeq下的range类
    //先保存了第一个元素
    println(list09)//Vector(5, 4, 2, -1, -5, -10)

    val list10 = (1 to 5).scanRight(5)(_ - _) //右扫描
    //先保存了第一个元素
    println(list10)//Vector(-2, 3, -1, 4, 0, 5)


    println("--------fold折叠:reduce就是简化版的fold,reduce=tail.foldLeft(head)(op)----")
    val list08 = List(1,2,3,4)
    //和reduce不同是传入了一个初始值，更多用法是传入一个collection
    //函数科里化
    println(list08.foldLeft(5)(_ - _)) //指定了初始值
    /*fold流程
    (5-1)
    (5-1)-2
    ((5-1)-2) -3
    (((5-1)-2) -3) -4
     */
    println(list08.foldRight(5)(_ - _))

  }
}
