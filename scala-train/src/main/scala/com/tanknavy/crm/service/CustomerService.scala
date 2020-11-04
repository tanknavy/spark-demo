package com.tanknavy.crm.service

import com.tanknavy.crm.bean.Customer

import scala.collection.mutable.ArrayBuffer

import util.control.Breaks._

class CustomerService {

  //数据放在内存里面，使用可变集合, ArrayBuffer
  var customerNum =1
  val customers = ArrayBuffer[Customer](new Customer(1,"Tom",'男',10,"139","tom@gmail.com"))

  def list(): ArrayBuffer[Customer] ={
      this.customers;
  }


  def add(customer:Customer) : Boolean = {
    customerNum += 1
    customer.id = customerNum
    customers.append(customer)

    true
  }

  def del(id:Int):Boolean={
    val index = findIndexById(id)
    if (index != -1 ){
      customers.remove(index) //找到就删除
      true
    }else{
      false //没有找到
    }
  }

  def findIndexById(id:Int) : Int = {
    //遍历ArrayBuffer
    var index = -1;//如果找到就该成对应的
    breakable { //动态的
      for (i <- 0 until customers.length) {
        if (customers(i).id == id) { //找到
          index = i
          break
        }
      }

    }
    index //默认-1表示没有，否则就是找到id
  }

}
