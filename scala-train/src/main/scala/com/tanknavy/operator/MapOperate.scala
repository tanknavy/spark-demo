package com.tanknavy.operator

/**
 * Author: Alex Cheng 9/18/2020 8:48 PM
 */
//问题：集合中每个元素 *2得到新集合
//模拟 map
object MapOperate {
  def main(args: Array[String]): Unit = {
    //1.传统方法
    println("------1.传统方法-----------")
    val list01 = List(3,5,7) //原集合
    var list02 = List[Int]() //新集合，不可变集合，
    for(item <- list01){ //遍历
      list02 = list02 :+ item * 2 //原集合不变，:+返回一个新集合，所以要var类型
      println(list02.hashCode())//三次不同地址
    }
    println(list02)

    println("------2.map映射-----------")
    //传统方法解决的问题
    //有点：处理方法直接，好理解
    //缺点：不够简洁高效，没有体现函数式编程：集合=>函数=>新的集合=>函数...
    //缺点：不利于处理复杂的数据处理业务
    //2.map映射方法
    //2.1map将list集合的元素一次遍历
    //2.2 将各个元素传递给multiple函数=>新的Int
    //2.3 将得到的新的Int放入到一个新的集合并返回
    //2.4 multiple会被调用n次

    val list03 = list01.map(multiple) //高阶函数，返回一个集合
    println(list01)
    println(list03)

    //filter过滤集合
    val names = List("Alice","Bob","Carol")
    val list05 = names.filter(startA _)
    println(list05)

    println("----------3.模拟map机制---------------")
    val myList = MyList() //注:要加(),不加相当于MyList这个object,加()表示应用相应apply方法
    println(myList.list1)
    //val list04 = myList.map(multiple)//可以带_
    val list04 = myList.map(multiple _)//后面带_表示不会执行这个函数，是函数类型
    println("MyList res=" + list04)


  }

  def multiple(n:Int):Int={
    println("multiple被调用~~")
    n * 2
  }

  def startA(s:String)={
    s.startsWith("A") //是否以A开头
  }

}

//深刻理解map映射函数的机制-模拟实现
class MyList{ //模拟集合
  val list1 = List(3,5,7,9) //源集合
  var list2 = List[Int]() //目标集合

  def map(f:Int =>Int): List[Int] = { //集合的map操作
    for(item <- list1){ //遍历源集合
      list2 = list2 :+ f(item) //函数应用结果append
    }
    list2 //最后返回map后的新集合
  }
}

object MyList{ //伴生对象
  def apply(): MyList = new MyList() //可以不new而使用MyList()就调用apply方法
}