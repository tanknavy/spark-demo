package com.tanknavy.collection

import scala.collection.mutable

/**
 * Author: Alex Cheng 9/18/2020 1:24 PM
 */

//散列表(java7数组+链表实现， java8以后数组+链表[大于8个元素使用tree])
//键值对(k/v), 无序的,key是唯一的,value可重复
object MapDemo {
  def main(args: Array[String]): Unit = {

    println("------1.不可以变的Map是有序的，创建和添加---------")
    //这个map中每个元素都是Tuple2类型, k/v类型支持Any
    val map = Map("alice" ->10, "bob" ->20, "carol" -> 30, "bob" ->22) //有序，Map[String, Int]泛型可写可不写
    println(map)


    println("------2.可变Map是无序的，创建和添加---------")
    val map2 = mutable.Map[String, Int]() //无序
    //val hm = new mutable.HashMap[String, Int]()
    map2.put("no1", 10)
    map2.put("no2", 20)
    map2.put("no3", 30)
    println(map2) //Map(no2 -> 20, no1 -> 10, no3 -> 30)

    //创建空的映射
    val hm = new mutable.HashMap[String, Int]() //无序
    hm.put("id1", 11)
    hm.put("id2", 22)
    hm.put("id3", 33)
    println(hm)

    //对偶元祖方式创建Map
    val map3 = mutable.Map(("Alice",11),("Bob",12),("Carol",13))
    println(map3)

    println("------3.Map取值---------")
    //key不存在在scala中抛出异常，在java中返回null!
    println(map3("Alice"))
    //println(map3("David")) //不存在抛java.util.NoSuchElementException,太粗暴
    if(map3.contains("David")){//检查一下
      println(map3("David"))
    }else{
      println("这个key不存在")
    }

    println("------3.2 map.get()取值返回Option对象---------")
    //注：map.get(key)返回Option对象，要么Some(进一步取值),要么None, Some再get
    println(map3.get("Alice")) //返回Some(11),还有getElse返回默认值
    println(map3.get("Alice").get)//Some中取具体值
    println(map3.get("David")) //get没有None，如果还要get，就抛异常
    //println(map3.get("David").get)//返回None再get就会异常
    println(map3.getOrElse("David","NoKey")) //默认值

    println("------4.Map修改添加---------")
    //修改：如果key存在就更新，不存在就是添加
    map3("Edwin") = 15 //更新或添加
    map3 += ("Frankie" ->16) //添加单个元素
    map3 += ("Giggs" ->17, "Henry" ->18) //添加单个元素
    map3("Edwin") = 25 //如果存在就更新
    println(map3)

    println("------5.Map删除元素---------")
    map3.remove("Giggs") //删除单个
    map3 -= ("Alice", "Frankie","Zoe") //删除多个
    val map4 = map3 - ("Alice", "Frankie") //可以这样删除多个重新赋值
    println(map3)


    println("------5.Map遍历，有4种性质---------")
    //(k,v), map.keys, map.values, map
    for((k,v) <- map3) println(k + " is mapped to " + v)
    for(k <- map3.keys) println("key:" + k)
    for(v <- map3.values) println("value:" + v)
    for(t <- map3) println("tuple in map: " + t + ",value:" + t._2) //Tuple2
  }

}
