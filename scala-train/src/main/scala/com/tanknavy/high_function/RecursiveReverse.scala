package com.tanknavy.high_function

/**
 * Author: Alex Cheng 9/13/2020 11:24 AM
 */
//递归反转字符串
object RecursiveReverse {
  def main(args: Array[String]): Unit = {
    var ss = "Hello World!"
    println(myReverse(ss))
    val num = 5
    println(factorial(num))
  }

  //递归套路三部曲，
  //1)类型：输入是类似loop中初始化的值，输出是最终希望的类型
  //2)先检测退出条件
  //3)每次只操作一个元素，无论是求和还是求极值
  //4)递归类型1:一种像是求sum一样逐个累加
  //4)递归类型2:一种像是对List中求极值，每次先处理head，
  // 同时假设剩下子序列(tail)都是合乎要求的，这时再拼装或者计算输出最终结果
  //5)纯函数方式，整个递归不依赖同一个外部var变量,
  def myReverse(ss:String): String ={ //反转字符串
   if(ss.length ==1) return ss else myReverse(ss.tail) + ss.head
  }

  //计算阶乘
  def factorial(num:Int): Int ={
    if(num == 0) return 1 else num * factorial(num-1) //就是当前数，然后假设剩余都处理好了
  }

  //求和
  def SumRecursive(num:BigInt, sum: BigInt):BigInt ={//不使用外部变量
    if(num <=99999999l){
      //巧妙，num不可变，传入+1不就可以了吗
      return SumRecursive(num + 1, sum + num)//即使传入num/sum是val类型
    }else return sum
  }

  //list = List(1,2,3),list.head返回1，list.trail返回(2,3)
  def myMax(list: List[Int]): Int = {
    if (list.isEmpty) //为空抛出异常
      throw new java.util.NoSuchElementException()
    else if (list.length == 1) //一个元素返回头
      return list.head
    else //头比剩余任何一个都大就返回head,否则递归
    //递归是告诉计算机做什么，而不是告诉计算机怎么多(迭代)
    // 最大值要么在head,要么在剩余的tail, 假设tail处理好了
    if (list.head > myMax(list.tail)) return list.head//最大值在头
    else return myMax(list.tail) //最大值在去处head的剩余tail
  }


}
