package com.tanknavy.implicit_conversion

/**
 * Author: Alex Cheng 9/14/2020 10:26 AM
 */

object ImplicitAddMethod {

  //隐式函数丰富类库功能
  //编写一个隐式函数，丰富mysql功能, 注意作用域
  //本来mysql对象没有delete方法，但是通过隐式函数返回DB类型，就能获取DB类的方法
  implicit def addDelete(mySQL: MySQL): DB = {
    println("implicit function is callled!")
    new DB
  }

  def main(args: Array[String]): Unit = {
    val mysql = new MySQL
    println("--------------------------")
    mysql.insert()
    //底层相当于调用addDelete(mysql).delete(),
    mysql.delete() //下划线表示隐式方法,

//    new Store
//    mysql.save()
  }

}

class MySQL {
  def insert(): Unit = {
    println("MySQL class insert method")
  }
}

class DB {
  def delete(): Unit = {
    println("DB class delete method")
  }

  def insert(name: String): Unit = {
    println("DB class insert method ", name)
  }
}

class Store(mysql: MySQL){
  def save():Unit= {
    println("Store class save method")
  }
}
