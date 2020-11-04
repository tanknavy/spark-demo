package com.tanknavy.demo;

/**
 * Author: Alex Cheng 6/14/2020 1:57 PM
 */
public class StringTest {
    String aa = new String("good");
    char[] bb = {'t','e','s','t'};

    public void change(String str, char[] chars){//str形式参数
        str = "test ok"; //str所代表的原来的字符串不可被改变
        System.out.println("str: " + str);
        chars[0] = 'b'; //字符串不可变？
    }
    public static void main(String[] args) {
        StringTest ex = new StringTest();
        ex.change(ex.aa, ex.bb);
        System.out.println(ex.aa);
        System.out.println(ex.bb);
    }
}
