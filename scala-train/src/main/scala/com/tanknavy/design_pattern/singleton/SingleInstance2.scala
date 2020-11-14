package com.tanknavy.design_pattern.singleton

//Scala没有static概念，所以使用类和伴生对象来实现

//将该类的构造方法设定为私有
class SingleInstance2 private() {}

//饿汉式,上来就先实例化一个对象，
object SingleInstance2 {
  private val s: SingleInstance2 = new SingleInstance2

  def getInstance() {
    s //返回该单例
  }
}


//将该类的构造方法设定为私有
class SingleInstance3 private() {}

//使用apply
object SingleInstance3 {
  private var s: SingleInstance3 = null

  def apply(): SingleInstance3 = {
    if (s == null) {
      s = new SingleInstance3()
    }
    s //返回
  }
}

//JDBC单例化？封装一个简单的内部数据库连接池
//private static JDBCHelper instance = null; //自身的实例
//
//public static JDBCHelper getInstance(){
//  if (instance == null){
//    synchronized(JDBCHelper.class){
//      if (instance == null){
//        instance = new JDBCHelper(); //保证在整个程序运行中，只会创建一次实例
//      }
//    }
//  }
//return instance;
//}

//大厂使用的双端检测
//将该类的构造方法设定为私有
class SingleInstance4 private() {}

//使用apply
object SingleInstance4 { //大厂常用的双端检测
  private var s: SingleInstance4 = null//懒汉式

  def apply(): SingleInstance4 = {
    if (s == null) { //如果不存在，准备加锁
      synchronized { //同步锁，使用synchronized修饰一个方法或者代码块，
        if (s == null) { //拿到锁时万一有人已经创建好了呢
          s = new SingleInstance4()
        }
      }
    }
    s //返回
  }
}


object TestSingleton2 {
  def main(args: Array[String]): Unit = {
    var s1 = SingleInstance2.getInstance()
    var s2 = SingleInstance2.getInstance()
    println(s1 == s2)

    var s3 = SingleInstance3()
    var s4 = SingleInstance3()
    println(s3 == s4)

    //模拟java的大厂双端检测
    var s4a = SingleInstance4()
    var s4b = SingleInstance4()
    println(s4a == s4b)

  }
}