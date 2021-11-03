package com.tanknavy.oop

/**
 * Author: Alex Cheng 9/14/2020 6:53 PM
 */

object ClassDemo {
  def main(args: Array[String]): Unit = {

    val cat = new Cat
    cat.name = "xiaobai" //编译器cat.name_$eq("xiaobai1")
    cat.color = "white"
    cat.age= 9
    println(cat.age)
    //--------属性的默认值-------------------
    var a = new A
    println(a.var1,a.var2, a.var3, a.var4)
    //-------对象的引用------------------
    val d1 = new Dog
    d1.name = "jack"
    val d2 = d1
    println("d2.name= " + d2.name)
    d1.name = "zoe"
    println("new d2.name= " +  d2.name)
    val d3 = new Dog
    //默认hashCode是object的地址转成int
    println(d1.hashCode(), d2.hashCode(), d3.hashCode())
    println(d1==d2, d1==d3)

    val t = {}
    println("t = " + t + " " + t.isInstanceOf[Unit])

    //scala很多集合都有reverse方法, Range也是一个集合
    for(i<- 1 to 3 reverse) println(i)
    (1 until 3).reverse.foreach(println)

    def myForeach[U](f: Int =>U)={

    }

    println("reflect --------------------------------")
    classOf[Person].getDeclaredFields.foreach{ field => println(field.getName)} //
    classOf[Person].getDeclaredFields.foreach{ field => field.setAccessible(true); println(field.getName)} //需要访问字段值时

    println(List.apply(classOf[Cat].getDeclaredFields.map(f => f.getName) : _*))
    classOf[Cat].getDeclaredFields.foreach(f => println(f.getName))
    println(List.apply(classOf[Cat].getDeclaredFields.map(f => f.getName) : _*).toString())

  }

  //java里面class属性值可不写， 会自动赋予初始值
  //scala的class默认public修饰，类可以嵌套类，但是创建时和java不同 new outer.innerClass, outer.new innerClass(java), new OuterClass.InnerClass(java static)
  //scala的class属性必须给初始值,要么显式，要么_给默认
  //默认修饰符是private, 属性会同时生成两个公共方法public name() <类似geter>, public name_() <类似setter>
  class Cat{
    //定义三个属性, 默认是public
    var name: String = "" //给初始值
    var color: String = _ //_表示给默认值,string默认值是null
    private var _age: Int = _ //_age表示私有字段，后面_表示Int默认0,

    def age= _age //getter, 没有形参
    def age_= (newValue: Int):Unit = { //定义setter方法,
      if (newValue < 100) _age = newValue else println("age must < 100")
    }
  }

  class Dog{
    var age:Int = _ //默认为0
    var name: String =  null //如果初识赋值为null, name一定要写类型，否则类型推断为Null!
    var address:String = ""//

    def cal(n1:Int, n2:Int): Int ={ //类方法
      return n1 + n2
    }
  }

  class A{
    var var1: String = _ //默认null
    var var2: Byte = _ //默认0
    var var3: Double = _ //默认0
    var var4: Boolean = _ //默认false
  }

}
