package com.tanknavy.design_pattern.proxy

import java.rmi.registry.LocateRegistry
import java.rmi.{Naming, RemoteException}
import java.rmi.server.UnicastRemoteObject

//继承UnicastRemoteObject,实现了RemoteRMI这个trait
class RemoteRmiImpl extends UnicastRemoteObject with  RemoteRMI {
  @throws(classOf[RemoteException])
  override def sayHello(): String = {
    "Hello World from RMI~"
  }
}

//服务注册
object RemoteRmiImpl{
  def main(args: Array[String]): Unit = {
    val service: RemoteRMI = new RemoteRmiImpl()
    //服务绑定到9999端口,两种方式
    //LocateRegistry.createRegistry(9999)
    //Naming.rebind("RemoteHello", service)
    Naming.rebind("rmi://192.168.1.187:9000/RemoteHello", service)
    println("远程服务开启，在127.0.0.1的9999端口监听，服务名RemoteHello")

  }
}