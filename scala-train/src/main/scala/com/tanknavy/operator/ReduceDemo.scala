package com.tanknavy.operator

/**
 * Author: Alex Cheng 9/19/2020 11:04 AM
 */

//scala函数推荐使用递归替代循环
//递归：不是告诉计算机怎么做，而是告诉计算机做什么
object ReduceDemo {
  def main(args: Array[String]): Unit = {

    //源码中reduce就是使用reduceLeft
    println("-----------reduce----------")
    val list01 = List(1,20, 30, 4, 5)
    val list02 = list01.reduce(sum _) //也可以直接传入匿名函数
    //val list02 = list01.reduce(_ + _) //匿名函数
    println(list02)

    println("-----------reduceLeft----------")
    val list03 = List(1,20, 30, 4, 5)
    val list04 = list03.reduceLeft(sum _)
    println(list04)
    /* reduce流程分析
      (1 + 20)
      (1 + 20) + 30
      ((1 + 20) + 30) + 4
      (((1 + 20) + 30) + 4) + 5
     */

    println("-----------reduceRight----------")
    val list05 = List(1,20, 30, 4, 5)
    val list06 = list05.reduceRight(sum _)
    println(list06)


    println("-----------几个reduce对比(减法)--------------")
    val list07 = List(1,2,3,4,5)
    println(list07.reduceLeft(minus)) //可以使用匿名函数
    println(list07.reduce(minus))
    println(list07.reduceRight(minus))

    println("-------------reduce求min------------------")
    println(list07.reduce(min))

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

    println("---------fold的缩写/:和：\\------------------------")
    println((5 /: list08)(minus)) //左折叠,：对着集合
    println((list08 :\ 5)(minus)) //右折叠

  }

  def sum(n1:Int, n2:Int) = {
    printf("n1: %d, n2: %d\n", n1, n2)
    n1 + n2
  }

  def minus(n1:Int, n2:Int) = {
    //printf("n1: %d, n2: %d\n", n1, n2)
    n1 - n2
  }

  def min(n1:Int, n2:Int)={
    //(n1 > n2)? n2:n1
    if(n1 > n2) n2 else n1
  }
}
