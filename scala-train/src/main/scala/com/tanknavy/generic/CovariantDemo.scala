package com.tanknavy.generic

/**
 * Author: Alex Cheng 9/20/2020 10:27 PM
 */

//协变，逆变，不变， 父子类的关系在泛型中继承
//C[+T]: 如果A是B的子类，那么C[A]是C[B的子类，称为协边(covariant)
//C[-T]: 如果A是B的子类，那么C[B]是C[A]的子类，称为逆变(contravariant)
//C[T]: 无论A和B是什么关系，C[A]和C[B]没有从属关系称为不变，java里面的泛型都是不变（invariant）

object CovariantDemo {
  def main(args: Array[String]): Unit = {

    val sub1: Sub = new Sub
    val sub2: Super = new Sub
    println(sub1.isInstanceOf[Sub])
    println(sub2.isInstanceOf[Sub])

    println("-------不变---------")
    val t1: Temp[Sub] = new Temp[Sub]("hello") //ok
    //val t2: Temp[Sub] = new Temp[Super]("hello") //没有关系，错误
    //val t3: Temp[Super] = new Temp[Sub]("hello") //没有关系，错误
    println(t1.toString)

    println("-------协变---------")
    val t4: Temp2[Sub] = new Temp2[Sub]("hello") //ok
    //val t5: Temp2[Sub] = new Temp2[Super]("hello") //错误
    val t6: Temp2[Super] = new Temp2[Sub]("hello") //协变,Temp[Sub]是Temp2[Super]的子类
    println(t6.toString)

    println("-------逆变---------")
    val t7: Temp3[Sub] = new Temp3[Sub]("hello") //ok
    val t8: Temp3[Sub] = new Temp3[Super]("hello") //逆变,Temp3[Super]是Temp3[Sub]的子类，反过来了
    //val t9: Temp3[Super] = new Temp3[Sub]("hello") //错误
    println(t8.toString)

  }
}

class Temp[A](title:String){ //不变
  override def toString:String = title
}

class Temp2[+A](title:String){ //协变
  override def toString:String = title
}

class Temp3[-A](title:String){ //逆变
  override def toString:String = title
}

class Super//父类
class Sub extends Super//子类