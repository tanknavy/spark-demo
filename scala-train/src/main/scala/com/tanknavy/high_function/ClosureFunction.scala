package com.tanknavy.high_function

/**
 * Author: Alex Cheng 9/12/2020 7:55 PM
 */

//这里的闭包就是python里面的@decoratro装饰器
object ClosureFunction { //返回内部函数，保存了外部变量
  def main(args: Array[String]): Unit = {
    var f = makeSuffix("jpg") //f就是闭包=函数加外部变量suffix
    println(f("dog.jpg"))
    println(f("cat"))
  }

  def makeSuffix(suffix:String)= {
    //闭包是一个带有外部环境的函数
    (filename:String) => {
      if (filename.endsWith(suffix)){
        filename
      }else{
        filename + suffix
      }
    }
  }

  /*
  def log(func): //函数log装饰里面的func函数
    def wrapper(*args, **kw): //这些参数是给里面用
        print('call %s():' % func.__name__) //装饰器的逻辑
        return func(*args, **kw) //正常执行原本的函数
    return wrapper //返回包裹后的处理逻辑

  @log //log函数装饰了myFunc函数
  def myFunc(x):
    print("myFunc:", x)

  相当于执行log(myFunc)
   */

}
