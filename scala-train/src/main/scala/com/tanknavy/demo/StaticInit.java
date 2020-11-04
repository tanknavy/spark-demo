package com.tanknavy.demo;

/**
 * Author: Alex Cheng 6/14/2020 12:15 PM
 */
public class StaticInit {
    private static int a = 10;
    static { //静态代码块
        a = 11;
        b = 21;
        System.out.println("static code a:" + a);
        //System.out.println(b); //非法前向引用
    }
    private static int b = 20; //b在后面定义，类初始化时按照顺序

    public static void main(String[] args) {
        System.out.println("Test01.a= " + a); //11
        System.out.println("Test01.b= " + b); //20
    }
}
