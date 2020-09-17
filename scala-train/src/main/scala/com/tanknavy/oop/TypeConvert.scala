package com.tanknavy.oop

/**
 * Author: Alex Cheng 9/16/2020 3:58 PM
 */

object TypeConvert {
  def main(args: Array[String]): Unit = {

    //--------向上，向下转型----------
    var person: Person = new Person
    val student = new Student
    val p2: Person = new Student //父类引用子类对象，将子类地址交给父类引用

    student.study()
    println(classOf[Student]) //类的全类名，类似java的Student.class
    println(student.getClass.getName) //反射方式
    println(student.isInstanceOf[Person]) //注：对student本身类型没有任何改变，只是返回了一个Person类型
    person = student.asInstanceOf[Person] //向上转型,对student本身类型没有任何改变，只是返回了一个Person类型
    person.showInfo() //向上转型使用父类方法没问题
    //person.study() //虽然是子类，但向上转型后已不能使用子类的方法！
    println(person.isInstanceOf[Student]) //向上转型后依然还是子类的实例！类似java的instanceOf
    person.asInstanceOf[Student].study() //向下转型后又可以调用子类的方法了！类似java的(Student)s2

    //--------------多态--------------------------
    println("----------------------------")
    val employee = new Employee
    testDuoTai(employee)
    testDuoTai(student)

    def testDuoTai(p: Person): Unit = { //p是Person及其子类都可以
      if (p.isInstanceOf[Student]) {
        p.asInstanceOf[Student].study() //转型后才有
      } else if (p.isInstanceOf[Employee]) {
        p.asInstanceOf[Employee].work()
      } else{
        println("转换失败")
      }

    }

  }
}
