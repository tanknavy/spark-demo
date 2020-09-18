package com.tanknavy.collection

/**
 * Author: Alex Cheng 9/17/2020 8:42 PM
 */

//scala里面集合默认都是immutable
object ArrayDemo {
  def main(args: Array[String]): Unit = {
    //---------1.不可变数组--------------
    //这个不可以变的Array怎么不用import就能用？它在scala包下面
    //泛型，[Any]表示该数组可以存放任意类型
    val arr01 = new Array[Any](6) //未赋值情况下为null
    arr01(0) = "aa"
    arr01(1) = 1
    arr01(2) = true
    arr01(3) = 'z'
    arr01(4) = 3.14
    println(arr01.length)
    println(arr01(0))

    arr01(1) = 3.14 //

    val arr02 = Array(1,3,"abc")//使用object Array的apply
    arr02(2) = 3.14 //

    //遍历,使用类似增强for循环
    for(i <- arr01){
      println(i)
    }
    println("-----------------")
    //遍历，使用index访问，
    for(index <- 0 until arr02.length){ //传统按照下标方式
      println(arr02(index))
    }

    //---------2.可变数组----------------------------
    println("-----------------")
    import scala.collection.mutable.ArrayBuffer //ctrl+b(browser)
    val arr03: ArrayBuffer[Int] = ArrayBuffer[Int](0)
    println(arr03.hashCode())
    //虽然是val修饰，但是为什么可以append元素改变这个array长度呢？
    //类似Java的array扩容，它创建了新的数组，将之前数组copy过来,返回了新的对象！
    arr03.append(1,2,3)//追加，支持可以变参数， append(elems:A*),A泛型，
    println(arr03.hashCode()) //追加元素后发现hashCode代表的地址发生变化
    val arr04 = arr03 :+  21 //Array(11,12,13) //:+ scala集合追加

    for(i <- arr03){
      println(i)
    }

    for(i <- arr04){
      println(i)
    }

    //-----3.可变数组的CRUD-------------
    arr03.remove(0)//按照index删除，这里删除0号位置
    for(i <- arr03){
      println(i)
    }
    println(arr03.length)

    //------4.不可以变和可变数组的相互转换--------------
    //底层实现，创建，复制，返回新对象
    val arr05 = arr03.toArray; //转Array
    val arr06 = Array[Int](1,2,3)
    val arr07 = arr06.toBuffer//转ArrayBuffer

    println("-----------5.多维数组-------------------")
    //创建
    val marr = Array.ofDim[Int](3,4) //Array.ofDim
    //遍历
    for(item <- marr){ //一维
      for(e <- item){ //二维
        println(e + "\t")
      }
      println()
    }
    //指定取出
    marr(1)(1) = 100
    println(marr(1)(1))
    for(item <- marr){ //一维
      for(e <- item){ //二维
        println(e + "\t")
      }
      println()
    }

    println("-------6.scala数组转java的list-------------------")
    val arr08 = ArrayBuffer("1","2","3")//
    //val arr08 = ArrayBuffer(1,2,3) //发现好像只能String
    import scala.collection.JavaConversions.bufferAsJavaList //隐式转换函数
    //使用ProcessBuilder
    val javaArr = new ProcessBuilder(arr08)//使用上述隐式函数
    val javaArrList = javaArr.command()
    println(javaArrList)

    println("----------------7.java的list转scala的buffer(ArrayBuffer)----------------")
    import scala.collection.JavaConversions.asScalaBuffer // asScalaBuffer[A](l: List[A]): Buffer[A]
    import scala.collection.mutable
    //java.util.List ==> Buffer
    val scalaArr: mutable.Buffer[String] = javaArrList //隐式转换，直接赋值的方式就转过来了
    scalaArr.append("jack","Tom")
    println(scalaArr)

  }
}
