package com.tanknavy.implicit_conversion

import java.awt.geom.{Ellipse2D, Rectangle2D}

/**
 * Author: Alex Cheng 9/14/2020 5:01 PM
 */
//trait继承一个类
//动态混入方式扩充类的功能，符合OCP原则

object TestTraitWith {
  def main(args: Array[String]): Unit = {
    //原本没有translate和grow两个方法，写一个特质继承一下
    //该类对象 with动态混入 特质的方法
    val egg = new Ellipse2D.Double(5,10,20,30) with RectangleLike//动态混入
    println("egg.x" + egg.x + " egg.y=" + egg.y)

    egg.translate(10, -10)
    println("egg.x" + egg.x + " egg.y=" + egg.y)
    egg.grow(10, 20)
    println("egg.x" + egg.x + " egg.y=" + egg.y)

  }

  //特质的继承
  trait RectangleLike extends Ellipse2D.Double{ //特质(类似接口)继承一个类
        def translate(x:Double, y:Double): Unit ={
          this.x = x //this继承自父类
          this.y = y
        }

        def grow(x:Double , y:Double) :Unit = {
          this.x += x
          this.y += y
    }
  }

  //特质继承, 第二种写法
  trait RectangleLike2 extends { //特质(类似接口)继承一个类
    this:Ellipse2D.Double =>
    def translate(x:Double, y:Double): Unit ={
      this.x = x //this继承自父类
      this.y = y
    }

    def grow(x:Double , y:Double) :Unit = {
      this.x += x
      this.y += y
    }
  }

}
