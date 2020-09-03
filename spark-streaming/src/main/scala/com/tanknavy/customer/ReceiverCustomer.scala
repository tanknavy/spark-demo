package com.tanknavy.customer

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket
import java.nio.charset.StandardCharsets

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

/**
 * Author: Alex Cheng 7/8/2020 1:59 PM
 * 自定义SparStream Receiver接收器
 */

//监控某个端口号，获取该端口号的内容，socket内容
class  ReceiverCustomer(host:String, port:Int) extends Receiver[String](StorageLevel.MEMORY_ONLY){

  //接收器方法
  override def onStart(): Unit = {
    //读取数据，传给spark框架
    new Thread("CustomerReceiver"){//启动先线程
      override def run(){
        //接受数据的方法
        receive() //不断从socket使用bufferReader
      }
    }.start() //启动
  }

  //接受数据，将数据传递给spark
  def receive(): Unit ={
    var socket:Socket = null

    var input:String = null //读的每行数据
    var reader:BufferedReader = null

    try{
      //创建socket对象
      socket = new Socket(host,port)
      //获取bufferReader, 输入流包装 socket->reader->buffer
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream, StandardCharsets.UTF_8) )//对socket输入流包装，nio

      //获取数据
      input = reader.readLine()

      while(input != null){
        store(input) //调用Receiver的store方法，将数据保存给spark
        input = reader.readLine() //继续读下一条
      }

    }catch {
      case e:Exception =>{ //读取socket数据异常时
        reader.close()
        socket.close() //接收器使用新线程，重启前关闭socket通信
        restart("重启!!!")
      }
    }

  }


  override def onStop(): Unit = {}
}
