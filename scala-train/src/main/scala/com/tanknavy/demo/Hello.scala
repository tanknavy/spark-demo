package com.tanknavy.demo

object Hello {
  def main(args: Array[String]): Unit = {
    println("hello scala")
    var dog = new Dog()
    dog.run()
  }
}

class Dog{
  def run():Unit = { println("dot is running")}
}