package com.tanknavy.design_pattern.singleton

//Scala没有static概念，所以使用类和伴生对象来实现

//将该类的构造方法设定为私有
class SingleInstance private (){

}

//懒汉式
object SingleInstance{ //伴生对象，包含该类的static属性和方法
  private var s: SingleInstance = null
  def getInstance(): Unit = {
    if(s == null){
      s = new SingleInstance //严谨一点加同步锁，里面再判断一次
    }
    s //返回该单例
  }
}

object  TestSingleton {
  def main(args: Array[String]): Unit = {
    var s1 = SingleInstance.getInstance()
    var s2 = SingleInstance.getInstance()
    println(s1 == s2)
  }
}