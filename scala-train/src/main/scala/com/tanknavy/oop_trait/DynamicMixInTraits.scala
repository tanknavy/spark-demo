package com.tanknavy.oop_trait

/**
 * Author: Alex Cheng 9/24/2020 3:32 PM
 */

//叠加特质：构建对象时混入多个特质，特质声明从左到右(父类依次从上到下)，方法执行从右到左(函数栈，super这时指左边的特质，如果左边没有特质就是自己super父类)
object DynamicMixInTraits {
  def main(args: Array[String]): Unit = {
    //在创建MySQL01实例时，同时混入DB01,File01两个特质

    //问题一：创建一个动态混入对象时，顺序是怎样的？
    //父特质DB01和File01都继承同一父特质，那么父trait构造器会被调用两次吗?
    //编译器在底层判断，前面父类的调用不可能调用两次，因为是一个对象!
    //顺序(mysql没有父类情况): 特质从左到右，其中每个还要从上到下一次构建父类构造器（父类不会被重复调用）
    println("-----------1.构建对象特质从左到右(其中父类调用从上到下)-------------")
    val mysql = new MySQL01 with DB01 with File01 //特质也有类的概念，创建对象并动态混入时，也要调用父类构造器

    println("-------------2.执行方法(方法栈，从右到左)-------------")
    //问题二: 当执行动态混入对象的方法是，其顺序是怎样的?
    //函数方法按照栈的顺序，动态混入，这是方法从右到左开始执行, 此时方法中super是指左边的特质
    mysql.insert(80)

  }
}


/*为了展示叠加特质，以下类的继承关系
            Operate02
                |
              Data01
              /   \
             /     \
           DB01    File01
 */
trait Operate02{
  println("Operate02...") //默认构造器会执行这个方法
  def insert(id: Int) //抽象方法
}

trait Data01 extends  Operate02{ //继承tr02特质
  println("Data01...")//默认构造器会执行这个方法
  override def insert(id: Int): Unit = { //实现父类方法，override这里可写可不写
    println("Data01向插入数据=" + id)
  }
}

trait DB01 extends  Data01{
  println("DB01 ....")
  override def insert(id: Int): Unit = { //重写父类方法，override这里必须写，因为是真的重写父类方法
    println("DB01向数据库插入数据=" + id)
    super.insert(id)//调用了insert方法(super在动态混入时，不一定是父类！！！可以是前一个叠加特质，如果没有再执行父类方法)
  }
}

trait File01 extends  Data01{
  println("File01 ....")
  override def insert(id: Int): Unit = { //重写父类方法，override这里必须写，因为是真的重写父类方法
    println("File01向文件插入数据="+ id)
    super.insert(id) //调用了insert方法(super在动态混入时，不一定是父类！！！可以是前一个叠加特质，如果没有再执行父类方法)
    //如果不想super这查找方法，想直接调用特质的方法，使用泛型super[],其中泛型必须是直接超类
    //super[Data01].insert(id) //
  }
}

//class MySQL01{ //普通类
class MySQL01 extends SQL01 { //普通类
  println("MYSQL01本尊 ...")
}

class SQL01{
  println("SQL...")
//  def insert(id: Int): Unit = { //实现父类方法，override这里可写可不写
//    println("SQL01插入数据=" + id)
//  }
}