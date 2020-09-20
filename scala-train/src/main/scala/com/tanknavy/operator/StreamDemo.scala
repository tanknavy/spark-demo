package com.tanknavy.operator

/**
 * Author: Alex Cheng 9/19/2020 9:24 PM
 */
//stream是一个无穷集合，在需要时动态产生
//The Stream is a lazy lists 流是一个懒列表
//scala中List使用::操作，Stream使用#::操作符，Stream.empty在表达式结尾
object StreamDemo {
  def main(args: Array[String]): Unit = {
    //创建流
    def numsForm(n:BigInt) : Stream[BigInt] = n #:: numsForm(n+1) //逐个递增,递归
    val s1 = numsForm(1)
    println(s1)
    println("head=" + s1.head) //取出第一个元素，使用last会造成死循环
    println(s1.tail) //取到尾部会触发新一个元素的计算Stream(1, 2, ?)
    println(s1)

    println(s1.tail.tail)
    println(s1.tail.tail.tail)//不能驱动
    println(s1.head)
    println(s1) //Stream(1, 2, 3, 4, ?)
    println("----------take(n)取前n个行程新的stream--------------")
    val s1a = s1.take(3) //s1的前三个元素作为新的stream
    println(s1a.tail.tail)

    //流里面只有初始化数字5，对这个集合map,还是返回Stream类型
    val res = numsForm(5)map(x => x * x)
    println(res) //Stream(25,?)

    println("-----tail.tail--------")
    var s2 = numsForm(1)
    for(i <- 1 to 3){
      println(i)
      println(s2.take(i))
      //s2 = s2.tail
    }
    println(s2)

    println("----------#::操作符流产生----------")
    val s3 = 1 #:: 2 #:: 3 #:: Stream.empty
    println(s3.tail)

  }
}
