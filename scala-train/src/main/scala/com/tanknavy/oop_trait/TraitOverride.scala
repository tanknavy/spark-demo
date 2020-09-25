package com.tanknavy.oop_trait

/**
 * Author: Alex Cheng 9/24/2020 5:02 PM
 */

//重写抽象方法，abstract修饰，用于mixin时super指左边特质而不是父类
object TraitOverride {
  def main(args: Array[String]): Unit = {
    println("-----------")
    //注意mixin顺序，由于有abstract override方法，
    //val mysql = new MySQL03 with File02 with DB02 //错误!,File02中super指父类，父类方法是抽象方法没有实现，所以new会出错
    val mysql = new MySQL03 with DB02 with File02 //File02中super是指左边的trait，相当于调用DB02的方法,
    mysql.insert(9)
  }
}


trait Operate03{
  def insert(id:Int) //抽象方法
}

//重写抽象方法，任然还是抽象方法---在动态混入中使用，super是前一个特质情况
trait File02 extends Operate03{ //继承并重写抽象方法，还声明式抽象方法
  //子特质中实现了父特质抽象方法，同时又调用super方法？相当于方法没有完全实现，必须加上abstract override
  //用处：在动态mixin中，super不一定是父类，如果这时super.insert(id)中super是指左边的特质，这时super就可以正常运行，
  //此时super调用和动态混入顺序有密切关系
  //def insert(id:Int):Unit = {
  abstract override def insert(id:Int):Unit = { //没有完全实现，需要abstract override
    println("数据保存到File02中...")
    super.insert(id) //子特质中实现了父特质抽象方法，同时又调用super方法？相当于方法没有完全实现
  }
}

trait DB02 extends Operate03 { //继承并实现
  override def insert(id: Int): Unit = {
    println("数据保存到DB02中...")
  }
}

class MySQL03{}