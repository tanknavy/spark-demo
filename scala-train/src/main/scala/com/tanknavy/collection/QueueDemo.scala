package com.tanknavy.collection

import scala.collection.mutable

/**
 * Author: Alex Cheng 9/18/2020 12:42 PM
 */

//1.队列是有序的，可以用数组也可以用链表实现，FIFO
object QueueDemo {
  def main(args: Array[String]): Unit = {
    import scala.collection.mutable.Queue
    println("1.-------Queue创建，追加元素-------------------")
    val q1 = new mutable.Queue[Int]
    q1 += 20 //添加一个元素到队列
    q1 ++= List(1,3,5) //列表元素展开放到队列后面
    //q1 += List(11,12,13)//泛型为Any才ok，因为是将list当做一个元素放入队列
    println(q1)

    println("2.----Queue出队和入队------")
    q1.dequeue()//出队，从头部
    println(q1)

    q1.enqueue(11,31,32) //入队，从尾部
    println(q1)


    println("3.---------Queue查看/不弹出队列元素,q不发生变化------------")
    //head/tail, take,drop, 这些都方便使用递归替代loop的函数式编程
    println(q1.head)
    println(q1.last) //最后一个元素
    println(q1.tail.tail.tail) //返回除了第一个以外的元素
    println(q1.take(2)) //前n个
    println(q1.drop(2))//排除前n个
    println(q1)
    println(q1.dropRight(2))//排除倒数2个

  }
}
