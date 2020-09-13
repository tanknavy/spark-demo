package com.tanknavy.high_function

/**
 * Author: Alex Cheng 9/12/2020 9:56 PM
 */
//函数科里化，接受多个参数都可以转化为接受单个参数
//一个动作完成一件事情，有点像流一样
object FuncCurry {
  def main(args: Array[String]): Unit = {
    //需求：接受两个数字计算乘积，观察演进
    //方法一：普通函数
    def m1(x:Int, y:Int) = x * y
    //方法二：闭包, 使用方式和curry一样！
    def m2(x:Int) = (y:Int) => x * y
    //方法三：curry科里化, 下面分两次才是真正的函数科里化
    def curry(x:Int)(y:Int) = x * y //科里化：一次传入一个参数完成一件事情

    println("normal function: ",m1(2,3))
    println("closure function: ",m2(2)(3))
    println("curry function: ",curry(2)(3))

    println("------------------------")
    val str1 = "HEllo" //String本省没有checkEq这个方法，使用隐式类
    val str2 = "heLLos"
    println("normal function: " + eq1(str1, str2))
    println("closure function: " + eq1(str1, str2))
    println("------------------------")

    //val t1 = eq3(str1,str2) //直接比较不等！！
    //val t1 = str1.checkEq(str2)((x1,x2) => x1 ==x2) //相等,实现了函数curry化！
    val t1 = str1.checkEq(str2)(eq3)//实现了函数curry化！
    val t2 = str1.checkEq(str2)(_.equals(_))//进一步简写，每个参数只出现了一次可以这样
    println("Real curry function: " + t2)
  }

  //函数科里化最佳实践
  //需求二：比较两个字符串在忽略大小写时是否相等
  def eq1(s1:String, s2:String)= s1.toLowerCase.equals(s2.toLowerCase)
  //闭包，不是科里化，没有实现一次做一件事情
  def eq2(s1:String)= (s2:String ) => s1.toLowerCase == s2.toLowerCase

  //科里化思想，分解成一次做一件事情，
  //隐式类：当创建隐式类中的对象时(这里是String类型)，就自动关联了它的(String)方法checkEq
  implicit class TestEq(s:String){//相当于对String类扩展了checkEq方法
    //先传入一个参数(s，第一个变量)，再传入另外一个参数(function,统一大小写)
    println("outer parameter:" + s)//测试
    //分解成两个任务：checkEq转换大小写，同时接受了一个f函数
    def checkEq(ss: String)(f:(String, String) =>Boolean):Boolean = {
      println("inner parameter:" + ss)//测试
      f(s.toLowerCase, ss.toLowerCase) //统一了大小写，比较交给另外一个函数处理，两件事情分开了！
    }
  }
  //有两件事，一是要统一大小写(使用隐式类)，二要比较大小，这个函数只做大小比较
  def eq3(s1:String, s2:String) = {s1.equals(s2)} //直接比较(使用隐式类)
  //有了上面的隐式类，String类型对象str1马上具有checkEq方法，
  //str1.checkEq(str2)((x1,x2) => x1 ==x2)
  //str1.checkEq(str2)(_.equals(_))//实现了函数curry化！


}
