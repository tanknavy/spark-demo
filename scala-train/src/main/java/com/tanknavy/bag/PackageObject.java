package com.tanknavy.bag;

/**
 * Author: Alex Cheng 9/15/2020 7:50 PM
 */
//模拟Scala中的包对象，

public class PackageObject {
    public static void main(String[] args) {
        System.out.println(Package$$.class.getName());
        System.out.println("--------------");

        System.out.println(Package$$.module$.name());//通过类的静态属性
        Package$$.module$.sayHi();

        System.out.println("--------------");
        Package$$ sp = new Package$$();
        System.out.println(sp.name());
        sp.sayHi();
    }
}

//模拟scala的包对象(包含一个name属性和一个方法)，关键是static module
//scala类编译时会产生两个文件，
//class中静态属性module, 然后这个属性使用今天
final class Package$${
    //定义属性，包括今天对象module$
    public static final Package$$ module$; //怎么初始化?
    //public static final Package$$ module$ = new Package$$(); //怎么初始化?

    private String name = "king"; //模拟scala包对象中的属性

    public Package$$() {
        System.out.println("blank constructor...");
    }

    static { //静态代码块调用构造方法完成静态对象module$的初始化！！
        System.out.println("static start...");
        module$ = new Package$$();
        System.out.println("static end...");
    };

    public String name(){ //属性的get方法
        return this.name;
    }
    public void sayHi(){ //模拟scala包对象中的方法
        System.out.println("scala package object method...");
    }
}
