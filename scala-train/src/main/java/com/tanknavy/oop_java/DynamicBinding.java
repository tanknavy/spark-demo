package com.tanknavy.oop_java;

/**
 * Author: Alex Cheng 9/16/2020 9:39 PM
 */

//在java中父类和子类的同名字段值，按照用什么引用就输出什么值，但是一下代码不一样
//java动态绑定机制的小结：子类对象地址，交给父类引用
//1.如果调用的是方法，则jvm就会将该方法和对象的内存地址绑定
//2.如果调用的是属性，则没有动态绑定机制，在哪里调用，就返回对应值
public class DynamicBinding {
    public static void main(String[] args) {
        //将一个子类的对象地址，交给父类的引用
        A a = new B(); //父类引用子类对象
        System.out.println(a.sum()); //40//30
        System.out.println(a.sum1());//30//20
    }
}

class A{
    public int i = 10;
    public int getI(){
        return i;
    }
    public int sum(){
        return getI() + 10;
    }
    public int sum1(){
        return i + 10;
    }
}

//屏蔽sum()和sum1()看看区别
class B extends A{
    public int i = 20;
    public int getI(){
        return i;
    }
//    public int sum(){
//        return getI() + 20;
//    }
//    public int sum1(){
//        return i + 10;
//    }
}