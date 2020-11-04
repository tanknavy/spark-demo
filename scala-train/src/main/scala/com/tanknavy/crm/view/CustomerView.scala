package com.tanknavy.crm.view

import com.tanknavy.crm.bean.Customer
import com.tanknavy.crm.service.CustomerService

import scala.io.StdIn
import util.control.Breaks.break

class CustomerView {

  //定义一个循环变量，空值是否退出
  var loop = true
  var key = ' '//接受用户输入

  val customerService = new CustomerService() //做成view的属性，下面可以反复使用

  def mainMenu(): Unit= {
    do{
      println("--------------客户信息管理系统----------------")
      println("              1 添加客户")
      println("              2 修改客户")
      println("              3 删除客户")
      println("              4 客户列表")
      println("              5 退出")
      println("请选择(1-5):")


      key = StdIn.readChar() //标准读入中读入一个字符
      key match{
        case '1' => this.add()
        case '2' => println("修改客户")
        case '3' => this.del()
        case '4' => this.list()
        case '5' => this.loop = false

      }

    }while(loop == true)
    println("退出CRM系统")

  }


  def list(): Unit = {
    println()
    println("--------------------客户列表---------------------------")
    println("编号\t\t姓名\t\t性别\t\t年龄\t\t电话\t\t邮箱")
    //for遍历，获取customerService的
    val customers = customerService.list()
    for (cutomer <-customers){
      //重写customer的toString方法
      println(cutomer)
    }
    println("------------------客户列表完成---------------------------")
  }

  def add(): Unit = {
    println("-------------添加客户-----------------")
    println("姓名:")
    val name = StdIn.readLine()
    println("性别:")
    val gender = StdIn.readChar()
    println("年龄:")
    val age = StdIn.readShort()
    println("电话:")
    val tel = StdIn.readLine()
    println("邮箱:")
    val email = StdIn.readLine()

    val customer = new Customer(name,gender,age,tel,email)

    customerService.add(customer)

    println("-------------添加成功-----------------")

  }


  def del(): Unit = {
    println("-------------删除客户(-1退出)-----------------")
    println("用户id:") //ArrayBuffer索引和id并不对应，新增一个方法findIndexById
    val userId = StdIn.readInt()
    if(userId == -1){
      println("-------------退出删除-----------------")
      return
    }

    var choice = ""
    var flag = true
    do{ //先来一次
      println("确认是否删除y/n:")
      //val choice = StdIn.readChar().toLower //输入为空时出错
      //val choice = StdIn.readChar()
      choice = StdIn.readLine() //什么都没输入时为空串
      println("您输入了 " + choice)
      if(choice.trim.toLowerCase == "y" || choice == "n".trim.toLowerCase){
        flag = false // 不想util.control.Breaks._
      }
    }while(flag)

    if (choice == None || choice == null || choice == ""){
      return
    }

    if('y' == choice.charAt(0) || 'Y' == choice.charAt(0) ){
    //if('y' == choice || 'Y' == choice ){
      if (customerService.del(userId)){
        println("-------------删除成功-----------------")
        return
      }
    }
    println("-------------用户不存在-----------------")//其它情况

  }


}
