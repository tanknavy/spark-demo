package com.tanknavy.high_function

/**
 * Author: Alex Cheng 9/13/2020 12:46 AM
 */

object ControlAbstract {
  def main(args: Array[String]): Unit = {
    //runThread就是一个抽象空值
    //接受参数没有输入，也没有输出，f:()=>Unit
    def runThread(f1: ()=> Unit)= { //参数是函数，没有输入和输出
      new Thread{
        override def run(): Unit={
          f1()
        }
      }.start()
      //    new Runnable {
      //      override def run(): Unit = {
      //        f()
      //      }
      //    }

    }

    //注意：这里runThread()使用括号还不行
    runThread { //没有输入和输出的函数f1定义
      () =>
        println("Begin work...")
        Thread.sleep(2000)
        println("Done")
    }

    println("------------------------------")
    //没有输入输出的简写，传入代码块
    def runThread2(f2: => Unit)= { //参数是函数，没有输入和输出
      new Thread{
        override def run(): Unit={
          f2 //没有输入就没有()
        }
      }.start()

    }

    //没有输入输出的继续简化，这里相当于传入代码跨
    runThread2{
      println("Begin work2...")
      Thread.sleep(2000)
      println("Done2")
    }

  }

}
