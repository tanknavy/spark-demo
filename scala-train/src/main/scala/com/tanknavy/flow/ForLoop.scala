package com.tanknavy.flow

/**
 * Author: Alex Cheng 11/13/2020 9:19 PM
 */

//scala的for循环特点， 一般用for， 不推荐用while
//if守卫,Range步长
object ForLoop {
  def main(args: Array[String]): Unit = {
    //scala支持for/while/do while
    for(i <- 1 to 3){ // to/until, until最后一个不包括,里面可以加for循环守卫
      println("i="+i)
    }

    for(i <- 1 to 5 if i %2 == 1){ //for循环if守卫, 不满足就continue下一次循环
      println("i=" + i)
    }

    for(i <- 1 to 5; j=5-i){ //for循环引入变量
      println("j="+j)
    }

    //1.双重for循环
    for( i <- 1 to 3; j= i * 2){ //scala双重for循环可以这样写， for()也可以写成for{}，(),{}都支持
      println("i=" +i + ",j=" +j)
    }

    //2.集合遍历
    var list = List("hello", 10, 30, "tom")
    for(item <- list){
      println("item=" + item)
    }

    //3.for循环步长控制,
    for(i <- Range(1,10,2)){ //通过Range控制步长2，Range是一个集合，伴生对象中有apply方法， 所以可以new也可以直接Range()
      println("i=" + i)
    }
    //不用Range使用for循环守卫，步长为3
    for(i <- 1 to 10 if i % 3==1){ //if守卫,因为if是关键词，所以if前面本来有;区分为两句话，这里;就可以省略
      println("i=" + i)
    }

    //测试
    var count, sum = 0;
    for(i <- 1 to 100 if i%9 ==0){ //
      count += 1;
      sum += i
    }

    val num = 6
    for(i <- 0 to num){
      printf("%d + %d = %d",i,(num-i),num) //格式化输出
    }


  }
}
