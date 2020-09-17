package com.tanknavy.collection

/**
 * Author: Alex Cheng 9/16/2020 9:45 AM
 */

//要么从写到main方法里面，要么extends App，App是个trait将代码块包裹执行
//scala的trait类似java的abstract class和interface
//需求: 将java中hashmap内容复制到scala的hashmap
object MapTest extends App { //继承了App后，就可以在这个类中执行代码
  import java.util.{HashMap =>JavaHashMap} //导入HashMap并重命名
  import scala.collection.mutable.{HashMap => ScalaHashMap, _}//导入全部并重命名
  //使用java中的集合，但是语法还是遵守scala
  val javaMap = new JavaHashMap[Int, String] //[Int, String]泛型
  javaMap.put(1,"one")
  javaMap.put(2,"two")
  javaMap.put(3,"three")
  println(javaMap.keySet().toArray().mkString(";")) //mkString输出集合或者迭代器的元素

  val scalaMap = new ScalaHashMap[Int, String]
  for(key <- javaMap.keySet().toArray){ //java的map的key集合转为数组，然后迭代
    scalaMap += (key.asInstanceOf[Int] -> javaMap.get(key)) //asInstanceOf[Int]强转,
  }
  println(scalaMap) //依次输出每个元素
}
