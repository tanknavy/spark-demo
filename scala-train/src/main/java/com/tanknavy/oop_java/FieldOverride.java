package com.tanknavy.oop_java;

/**
 * Author: Alex Cheng 9/16/2020 9:06 PM
 */
//java中父类和子类相同字段，用什么引用就返回什么类型中的字段，在scala中全部都是子类字段的值
//和动态绑定案例对比一下
public class FieldOverride {
    public static void main(String[] args) {
        MySub mySub = new MySub();
        System.out.println(mySub.s);

        MySuper mysub2 = new MySub();
        System.out.println(mysub2.s);

    }
}

class MySuper{
    String s = "super";
}

class MySub extends MySuper{
    String s = "sub";
}
