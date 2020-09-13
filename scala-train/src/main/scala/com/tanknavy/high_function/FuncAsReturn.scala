package com.tanknavy.high_function

/**
 * Author: Alex Cheng 9/12/2020 4:15 PM
 */
//返回函数的函数，这就导致了闭包，函数科里化
object FuncAsReturn {
  def main(args: Array[String]): Unit = {

    //高阶函数二：返回匿名函数 (y:Int) => x-y, 返回的匿名函数可以使用变量接受
    def minusXY(x: Int)= {
      (y: Int) => x-y //返回匿名函数, 内部函数保存了外部变量！不是引用和javascript不一样
    }

    //来个三层的，比如先求面积，再体积，x*y*z
    def cube(x: Int)={ // Int => Int => Int
      def square(y: Int)={
        (z: Int) => x * y * z
      }
      square(_) //括号可以省略
      //square _ //或者square(_)两种写法都可以
    }
    println(cube(2))
    println("cube: " + cube(2)(3)(4)) //求体积

    //来个三层的函数作为参数， (num)(func)(func)
    def s3Func(x:Int)={
      def s2Func(f1: Int => Int) ={
        (f2: Int =>Int) => f1(f2(x))
      }
      s2Func(_) //显示的将方法转函数
    }
    val res_s3 = s3Func(4)(x=> x*2)(y=> y+3) //依次number/func/func
    val res_s4 = s3Func(4)((x:Int)=> x*2)((y:Int)=> y+3) //依次number/func/func

    def aa(n:Int) = {n * 2}
    def bb(n:Int)= {n + 3}
    val res_s3b = s3Func(4)(x=>x*2)(y=>y*3) //依次number/func/func
    println("res_s3: " + res_s3)
    println("res_s4: " + res_s4)
    //val res_s3 = s3Func(3)(x:Int => x * 2)(y:Int => y + 3)


    val f1 = minusXY(5) //这时f1就是 (y: Int) => 5 -y, 这个5是固定不变的！！！和javascript不变
    println(f1) //返回function1,有一个变量的函数
    val res = f1(3)
    println(minusXY(10)(6))//一步到位，保存第一个变量到返回函数中，再继续调用返回的函数
    println(res)

    func2Arr() //测试返回函数是否值绑定外部变量，是的！！！

    //function curry
    println(minusXY(10)(6))


    //scala这里都是值传递，javascript都引用类型，python匿名用引用传递，具体用值传递！！
    //三种语言三种效果！！！
    def func2Arr(): Unit ={
      import scala.collection.mutable.ArrayBuffer
      var arr = ArrayBuffer[Int => Int]()
      import scala.collection.mutable.ListBuffer
      var list = ListBuffer[Int => Int]()

      val iter: Range.Inclusive = 11 to 13
      for (i <- iter) { //循环变量
         //arr += minusXY(i) //将函数(闭包：函数+当前外部变量)放入集合
         arr += ((x: Int) => i-x) //使用匿名函数实
         list += minusXY(i) //将函数(闭包：函数+当前外部变量)放入集合

      }
      println(arr.toList, list)

      for(f <- arr){
        println("high-order(=>anonymou)function: ", f(2))
      }
      println("----具体vs匿名函数：scala都是值传递，js都是引用传递，python具体函数是值传递，lambda函数是引用传递！！---------")
      for(f <- list){
        println("high-order(concrete) function: ", f(5))
      }
    }

  }
}
