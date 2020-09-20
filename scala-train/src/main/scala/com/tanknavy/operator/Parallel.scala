package com.tanknavy.operator

/**
 * Author: Alex Cheng 9/19/2020 11:09 PM
 */

//并行集合par
//注意foreach总是discard结果
object Parallel {
  def main(args: Array[String]): Unit = {
    val num = 100
    println("------串行计算-----")
    (1 to 5).foreach(println) //和下面同一个效果，注意foreach总是discard结果,所以下面使用map
    //(1 to 5).foreach(println(_))
    val res1 = (1 to 5).map{case _ => Thread.currentThread().getName} //全部是main
    println(res1)

    println("------parallel collection并行计算-----")
    //println任务分配给不同cpu
    (1 to 5).par.foreach(println(_)) //发现没有按照顺序，体现并行计算
    //ParVector(ForkJoinPool-1-worker-5, ForkJoinPool-1-worker-7, ForkJoinPool-1-worker-1, ForkJoinPool-1-worker-3)
    val res2 = (1 to num).par.map{case _ => Thread.currentThread().getName} //发现没有按照顺序，体现并行计算
    println(res2.distinct)
  }
}
