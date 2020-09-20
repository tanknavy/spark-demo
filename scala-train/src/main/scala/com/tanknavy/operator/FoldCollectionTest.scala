package com.tanknavy.operator

/**
 * Author: Alex Cheng 9/19/2020 12:23 PM
 */

//Map都是使用apply，使用fold(collection)(f:) //不停向集合添加一个元祖
object FoldCollectionTest {
  def main(args: Array[String]): Unit = {

    println("-------foldLeft-----------")
    val sentence = "AAABBBBBCCDDDDEEZ"
    import scala.collection.mutable.ArrayBuffer
    val arr = new ArrayBuffer[Char]() //可变数组
    val arr2 = new ArrayBuffer[Char]()

    //arr作为头部，不停将新元素放到里面，函数一定要有返回值用于一轮迭代
    sentence.foldLeft(arr)((arr:ArrayBuffer[Char], c:Char) => {arr.append(c); arr}) //向可变数组中逐个放入
    println(arr)

    val arr3 = sentence.scanLeft(arr2)((arr:ArrayBuffer[Char], c:Char) => {arr.append(c); arr}) //向可变数组中逐个放入
    //println(arr3)

    println("-----floadLeft统计字符频率（不可变map）--------------")
    val charMap =  Map[Char,Int]() //不可变的
    def charCount(map: Map[Char, Int], c: Char)= {
      map + (c -> (map.getOrElse(c,0) + 1) ) //原map不变，每次都产生新map, map +()返回一个新map， getOrElse的使用,key存在就是更新
    }

    val charMap2 = sentence.foldLeft(charMap)(charCount)
    println(charMap2)

    println("-----floadLeft统计字符频率（可变map）--------------")
    import scala.collection.mutable
    val charMap3 =  mutable.Map[Char,Int]() //不可变的
    def charCount2(map: mutable.Map[Char, Int], c: Char)= {
      //map + (c -> (map.getOrElse(c,0) + 1) ) //原map不变，每次都产生新map, map +()返回一个新map， getOrElse的使用,key存在就是更新
      map += (c -> (map.getOrElse(c,0) + 1) ) //可变map
    }

    sentence.foldLeft(charMap3)(charCount2) //此时charMap3是可变的已经有值了
    println(charMap3)

    println("-------------大数据经典wordcount----------------")
    val lines = List("hello scala for spark", "scala spark", "hello spark")
    val splitted = lines.flatMap(s => s.split(" "))
    //val wordMap = mutable.Map[String, Int]()

    def wordCount(map: mutable.Map[String, Int], word:String) ={
      map += (word ->(map.getOrElse(word, 0) +1))
    }
    val wordMap = splitted.foldLeft(mutable.Map[String, Int]())(wordCount _)
    println(wordMap)
    //scala中map如何按照key排序
    import scala.collection.immutable
    //使用ListMap排序ListMap(map.toSeq.srotBy(_._2))
    //sorted, sortBy, sortWith
    //val wordRes = mutable.ListMap(wordMap.toSeq.sortBy(_._2):_*)//可变map是无需的
    val wordRes = immutable.ListMap(wordMap.toSeq.sortBy(_._2):_*) //不可变map是有序的
    val wordRes2 = immutable.ListMap(wordMap.toSeq.sortWith(_._2 > _._2):_*) //降序排列
    println(wordRes) //顺序
    println(wordRes2)

  }
}
