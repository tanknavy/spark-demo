package com.tanknavy.operator

/**
 * Author: Alex Cheng 9/19/2020 11:30 PM
 */

//操作符重载，操作符可以当做函数使用
object OperatorOverride {
  def main(args: Array[String]): Unit = {
    val n1 = 1
    val n2 = 2
    val r1 = n1 + n2 //Int中对大量操作符不同数据类型进行了重载
    val r2 = n1.+(n2)
    println(r1,r2)

    val monster = new Monster
    monster + 10 //对+重载, 中置操作符
    monster.+(15)
    println(monster.money)
    monster ++; //后置操作符
    println(monster.money)
    monster.++; //26
    println(monster.money)
    !monster //取反,前置操作符
    println(monster.money) //-27

  }
}

//类的操作符重载
class Monster{
  var money:Int = 0

  //+操作符override(中置操作符)，a +=
  def +(n:Int)={
    this.money += n
  }

  //++(后置操作符重载) a++
  def ++(): Unit ={
    this.money += 1
  }

  //前置操作符, 取反使用unary_!
  def unary_!(): Unit ={ //取反，一元运算符
    this.money = -this.money
  }
}