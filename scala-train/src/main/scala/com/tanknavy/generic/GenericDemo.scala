package com.tanknavy.generic

/**
 * Author: Alex Cheng 9/20/2020 9:15 PM
 */

//scala泛型, 希望函数参数可以接受任意类型
//java泛型实现时擦拭法(type erase),虚拟机代码执行并没有泛型，泛型是由编译器在编译时实行的
//编译器永远把T视为Object处理，但在需要转型时，会根据T的类型自动安全的强制转型
//java泛型的局限
//1.<T>不能是基本类型，应为Object类型无法持有基本类型
//2.对于泛型实例，无论T的类型,getClass()返回同一个Class实例，因为编译后全部都是MyClass<Object>
object GenericDemo {
  def main(args: Array[String]): Unit = {

    println("---------------1.泛型[T]-------------")
    val intMessage = new IntMessage[Int](9)
    println(intMessage)

    val stringMessage = new StringMessage[String]("hello",123)
    println(stringMessage)
    println(stringMessage.get)

    println("-------------2.多个泛型[A,B,C]----------------")
//    import com.tanknavy.generic.SeasonEnum //无法入到枚举类
//    val class01 = new EnglishClass[SeasonEnum.SeasonEnum , String, String](SeasonEnum.spring, "0210班", "高级版")
//    println(class01.season + "-" + class01.name + "-" + class01.classType)
//
//    val class02 = new EnglishClass[SeasonEnum.SeasonEnum , String, Int](SeasonEnum.spring, "0210班", 1)
//    println(class02.season + "-" + class02.name + "-" + class02.classType)

    println("----------3.函数中泛型----------")
    val list1 = List("hello","dog", "cat")
    val list2 = List(19, 23, 89)
    println(midList[String](list1))
    println(midList(list2)) //翻新类型可写可不写

  }

  //需求3：函数可以获取各种类的List的中间index的值
  def midList[E](list:List[E]) ={ //函数输入泛型E,返回泛型E
      list(list.length / 2) //列表中中间元素的值
  }
}

//需求1: Message类，可以构建Int类和String类型，使用泛型(不能使用Any)
//定义一个abstract class,使用泛型T
abstract class Message[T](s: T){ //抽象类，有构造函数
  def get = s
}

class IntMessage[Int](v:Int) extends Message(v) //继承
class StringMessage[String](v: String, v2: Int) extends Message(v)//继承

//需求2：英语班类，有三种类型
//class EnglishClass[A,B,C](season:A, name:B, classType:C)//字段没有修饰符默认是局部变量，不是属性
class EnglishClass[A,B,C](val season:A, val name:B, val classType:C)//val修饰符就会是只读属性

//season是枚举类型
class SeasonEnum extends Enumeration{ //枚举
  type SeasonEnum = Value //取别名
  val spring, autumn, summer, winter = Value //四个枚举
}


