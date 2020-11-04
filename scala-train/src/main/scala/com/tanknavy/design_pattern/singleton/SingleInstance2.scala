package com.tanknavy.design_pattern.singleton

//Scala没有static概念，所以使用类和伴生对象来实现

//将该类的构造方法设定为私有
class SingleInstance2 private() {}

//饿汉式,上来就先实例化一个对象，
object SingleInstance2{
  private val s: SingleInstance2 = new SingleInstance2
  def getInstance() {
    s //返回该单例
  }
}



//将该类的构造方法设定为私有
class SingleInstance3 private() {}

//使用apply
object SingleInstance3{
  private var s: SingleInstance3 = null
  def apply(): SingleInstance3 = {
    if (s == null){
      s = new SingleInstance3()
    }
    s //返回
  }
}

object  TestSingleton2 {
  def main(args: Array[String]): Unit = {
    var s1 = SingleInstance2.getInstance()
    var s2 = SingleInstance2.getInstance()
    println(s1 == s2)

    var s3 = SingleInstance3()
    var s4 = SingleInstance3()
    println(s3 == s4)

  }
}