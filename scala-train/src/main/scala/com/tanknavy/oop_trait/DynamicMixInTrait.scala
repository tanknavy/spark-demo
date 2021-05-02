package com.tanknavy.oop_trait

/**
 * Author: Alex Cheng 5/2/2021 3:10 PM
 */

object DynamicMixInTrait {
  def main(args: Array[String]): Unit = {
    val t1 = new Operation with Doubling with Incrementing //动态混入
    //从右到左，即使trait中调用super，也是调用它最左边的方法而不是父类方法
    println(t1.func(5)) // 12,先+1再double1

    val t2 = new Operation with Incrementing with Doubling
    println(t2.func(5)) // 11,  先double再+1

  }

  abstract class Operation{ //抽象方法
    def func(x: Int):Int = x
  }

  trait Doubling extends Operation{ //trait继承abstract class, 重写方法
    abstract override def func(x: Int): Int = super.func(x * 2)
  }

  trait Incrementing extends Operation{ //trait继承abstract class, 重写方法
    abstract override def func(x: Int): Int = super.func(x + 1)
  }

}
