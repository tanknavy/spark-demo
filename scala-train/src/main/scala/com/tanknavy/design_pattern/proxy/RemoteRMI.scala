package com.tanknavy.design_pattern.proxy

import java.rmi.{Remote, RemoteException}

//接口，定义抽象方法,RMI可以将从某个JVM上的对象调用另外一个JVM中的对象的方法，屏蔽了socket编程底层
//RMI设计面向对象的,RPC是面向过程的
trait RemoteRMI extends Remote{

  //一个抽象方法
  @throws(classOf[RemoteException]) //相当于RemoteException.class
  def sayHello():String

}
