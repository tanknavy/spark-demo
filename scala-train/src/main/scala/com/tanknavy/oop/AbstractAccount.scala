package com.tanknavy.oop

/**
 * Author: Alex Cheng 9/15/2020 10:04 PM
 */

object AbstractAccount {
  def main(args: Array[String]): Unit = {
    val account = new Account("ac32", 30000000, "09152020")
    account.query("09152020")

  }
}

//银行账户类,抽象属性和方法
class Account(inAccount:String, inBalance:Double, inPwd:String){
  private val accountNo: String = inAccount
  private var balance:Double = inBalance
  private var pwd: String = inPwd

  //方法
  def query(pwd:String): Unit ={
    if (!this.pwd.equals(pwd)){
      println("密码错误")
      return
    }
    printf("账号为%s,当前余额%f", this.accountNo, this.balance)
  }

  def withdraw(pwd: String, money: Double): Any ={
    if(!this.pwd.equals(pwd)){
      println("密码错误")
      return //return除了返回值，还要终止程序的作用
    }
    if (this.balance < money){
      println("余额不足")
       return
    }

    this.balance -= money
    return money;

  }
}
