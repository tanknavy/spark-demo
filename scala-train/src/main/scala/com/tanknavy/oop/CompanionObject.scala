package com.tanknavy.oop

/**
 * Author: Alex Cheng 9/17/2020 9:57 AM
 */

//伴生类和伴生对象
//1.java的static属性和方法是属于类对象的，不是通过对象调用的
//2.scala中的使用伴生对象模拟了java的static
//3.将静态内容写到伴生对象
//4.将非静态内容写到伴生类
//5.class Person编译后底层生成Person类和Person.class文件
//6.object Person编译后底层生成Person$类和Person$.class文件
object CompanionObject {
  def main(args: Array[String]): Unit = {
    println(ScalaPerson.sex) //等价于ScalaPeron$.MODULE$.sex()，MODULE$是一个静态对象
    ScalaPerson.sayHi() //ScalaPerson$.MODULE$.sayHi()

    println("------静态属性--------")
    val k1:Child = new Child("adm")
    val k2:Child = new Child("bob")
    val k3:Child = new Child("carl")
    Child.joinGame(k1)
    Child.joinGame(k2)
    Child.joinGame(k3)
    Child.showNum()

    println("-------伴生对象的apply方法-------------")
    val t1 = new Toy("cat")
    val t2 = Toy() //不使用new这种方式会自动调用对应的apply方法
    val t3 = Toy("dog")//apply方法
    t1.showInfo()
    t2.showInfo()
    t3.showInfo()
    
  }
}

//在同一个文件中，有class Person和object Person相互关系
//伴生类
class ScalaPerson{
  var make:String = _
}

//伴生对象
object ScalaPerson{
  var sex:Boolean = true
  def sayHi()={
    println("companion object say hi")
  }
}

//在伴生对象中定义apply方法可以实现类名(参数)创建对象实例
//val list = List(1,2,3)
class Toy(inName:String){
  var name:String = inName
  def showInfo(): Unit ={
    println("this is " + name + " toy")
  }
}
object Toy{ //可以直接Toy()会自动调用对应apply方法
  def apply(name:String): Toy = new Toy(name)
  def apply(): Toy = new Toy("匿名")
}


//----------小孩玩游戏----------------
class Child(inName:String){
  var name = inName
}

object Child{
  var count:Int = 0//模拟静态属性，

  def joinGame(c:Child)={
    printf("%s小孩加入了游戏\n",c.name)
    count += 1
  }

  def showNum()={
    printf("当前有%d个小孩在玩游戏\n", count)
  }
}
