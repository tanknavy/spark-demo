package com.tanknavy.collection

/**
 * Author: Alex Cheng 9/18/2020 10:15 AM
 */

//List属于Seq, 是有序的
object ListDemo {
  def main(args: Array[String]): Unit = {
    //说明
    //1.默认情况下，List是scala.collection.immutable.List，
    //2.那为啥不import就能直接使用呢？scala使用了包对象 val List = scala.collection.immutable.List
    //3.Nil在scala包对象中也有
    val list01 = List(1,2,3,4,5)
    //val list01 = List[Int](1,2,3,4,5)
    println(list01)

    val list02 = Nil //Nil表示空集合List()里面内容是空的，和null不同
    println(list02)

    println("--------------List集合的元素访问----------------")
    val v01 = list01(1)
    println(v01)

    println("--------------List集合的元素追加,本身集合并没有变化----------------")
    //想列表中增加元素，会返回新的列表/对象集合
    val list03 = list01 :+ 15 //：+表示在列表后面加
    println(list03)

    val list04 = 11 +: list01 // +:表示在列表前面加，:永远对着集合
    println(list04)

    println("-------::, :::符号--------------------")
    //先Nil空的list，集合必须在最右边，运算规则从右先左放入元素，放在前面！
    val list05 = 14::15::16::list01::Nil //两个:, 将列表后添加元素
    val list06 = 14::15::16::list01:::Nil //三个:::将集合每个元素加到集合，展开了
    val list07 = 14::15::16::list01 //两个:, 将列表后添加元素
    //val list08 = 14::15::16:::list01 //错误，:::左右两头都必须为集合，前面要展开放到后面
    val list08 = 14::15::16::list01:::list01 //
    println(list05)
    println(list06)
    println(list07)
    println(list08)
    println("---------------")

    println("+++++++++++++++++++++++++++++++++++++++++++++")
    println("---------------ListBuffer可变列表-------------")
    import scala.collection.mutable.ListBuffer
    val lb01 = ListBuffer[Int](1,2,3)
    val lb02 = ListBuffer[Int](21,22,23)
    println(lb01)

    //ListBuffer添加元素方式，
    // 还可以像不可以变的List使用:+,+:， 不可以变List不能使用像+=, ++=带=的操作
    lb01 += 4 //可变列表，添加单个元素
    lb01 :+ 5 //也可以像List不可以变集合一样追加元素
    6 +: lb01 //也可以像List不可以变集合一样前面元素
    lb01.append(15,16) //可添加多个元素

    lb02 ++= list01 //将右边集合的元素打算了追加到左边集合中
    val lb03 = lb01 ++ lb02 //++合并两个集合，比如批量增加
    val lb04 = lb01 :+ 5 //lb01不变, lb01集合和5合并赋给lb04

    //删除元素
    lb03.remove(1)//下标为1的元素删除

    println(lb02)
    println(lb03)
  //遍历
    for(item <- lb01){
      println(("item=" + item))
    }

  }
}
