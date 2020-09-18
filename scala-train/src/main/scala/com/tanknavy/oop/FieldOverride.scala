package com.tanknavy.oop

/**
 * Author: Alex Cheng 9/16/2020 9:10 PM
 */

//父类和子类相同字段，java中没有复写是隐藏实现的，而scala又override
//java动态绑定机制的小结：子类对象地址，交给父类引用
//1.如果调用的是方法，则jvm就会将该方法和对象的内存地址绑定
//2.如果调用的是属性，则没有动态绑定机制，在哪里调用，就返回对应值
object FieldOverride extends App{
  var sub: Sub2 = new Sub2
  println(sub.name)

  //和java中结果不同，是因为scala中对字段自动生成setter/getter,所以访问属性是调用方法
  //在jvm中动态绑定机制，调用的是方法，则该方法和对象的内存地址绑定
  //super2.s => super2.name()
  var super2: Super2 = new Sub2
  println(super2.name) //还是子类的字段值，和java不一样

  println("-----------------------")
  println(super2.sal()) //重写ok

}

class Super2{
  //var name:String = "super"
  val name:String = "super" //生成public s()
  def sal(): Int = {
    return 10
  }
}
class Sub2 extends Super2{ //override的必须是val不可变字段！！
  //override var name:String = "sub"//scala中必须override复写父类字段
  override val name:String = "sub"//scala中必须override复写父类字段
  //重写父类方法，使用val修饰
  override val sal:Int = 19 //看起来好像是变量重写了父类方法，实际是可以的！
}


abstract class A2{
  //1.抽象字段(属性)，就是没有初始化的字段，要求类也是抽象类
  //2.java中字段会自动初始化，scala中要求显式初始化，或者写成abstract类
  //3.对于抽象的属性，在底层不会生成对应的属性声明，而是生成两个对象的抽象方法(name, name_$eq)
  var name:String //抽象字段(属性)，就是没有初始化的字段
  val age:Int = 10
}

class SubA extends A2{
  //1.子类重写父类的抽象属性，本质是实现了抽象方法！！
  //2.这个override可以写也可以不写，本质是方法的实现！
  var name: String = "subA" //override可写可不写
  override val age:Int = 20

}