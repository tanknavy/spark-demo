package com.tanknavy.oop_java;

/**
 * Author: Alex Cheng 9/16/2020 11:08 AM
 */

//scala中属性都是通过方法去做，更严谨的方式，
//java里面方法有动态绑定机制，属性没有
//scala中，子类继承了父类的所有属性，但是private属性和方法不能访问，没有公共方法
//java中；先隐式或显示调用父构造器->再运行自己的构造器
//scala中；先必须调用主构造器，主构造器会调用父构造器，辅助构造器不容许调用父构造器
public class ExtendsTest {
    public static void main(String[] args) {
        Student student = new Student();
        student.name = "bob"; //调用了student.name()方法
        //System.out.println(student.tax);//java中私有字段不可以被继承
        student.showInfo();
        student.study();
        student.getTax();

        System.out.println("---------------------");
        System.out.println(Student.class); //返回全类名
        System.out.println(student.getClass().getName());
        System.out.println(student instanceof Person); //instancof类型判断
    }
}

class Person{
    public String name;
    public int age;
    private double tax = 0.0; //java中私有字段不可以被继承

    public Person() {
        System.out.println("Person non-para constructor");
    }
    //如果写了构造方法，那么将不使用默认的构造方法
    public Person(String name, int age, double tax) {
        this.name = name;
        this.age = age;
        this.tax = tax;
    }

    public void showInfo(){
        System.out.println("name: " + name);
    }

    public double getTax(){
        return this.tax;
    }

}

class Student extends Person{
    //子类默认调用父类的构造方法,优先Person()
    public Student() {
        System.out.println("Student non-para constructor");
    }

    //一旦写了构造方法，那么默认无参构造方法会失效
    public Student(String name, int age, double tax) {
        super(name, age, tax);
        System.out.println("Student call super constructor");
    }

    public void study(){
        System.out.println(name + " studying...");
    }
}

