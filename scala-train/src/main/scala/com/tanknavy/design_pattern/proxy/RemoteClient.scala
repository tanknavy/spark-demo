package com.tanknavy.design_pattern.proxy

import java.rmi.Naming

class RemoteClient {
  def go() = {
    val service = Naming.lookup("rmi://127.0.0.1:9000/RemoteHello").asInstanceOf[RemoteRMI]
    val str = service.sayHello()
    println("str = " +str)
  }
}

object RemoteClient{
  def main(args: Array[String]): Unit = {
    new RemoteClient().go()
  }
}
