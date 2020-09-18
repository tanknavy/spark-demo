package com.tanknavy.collection_java;

/**
 * Author: Alex Cheng 9/17/2020 9:16 PM
 */
public class ArrayTest {
    public static void main(String[] args) {
        Object[] objects = new Object[6];
        objects[0] = 1;
        objects[1] = 3.14;
        objects[2] = true;
        objects[3] = 'A';
        objects[4] = "hello";
        objects[5] = new int[3];

        for (Object object : objects) {
            System.out.println(object);
        }

        //可以改变类型吗
        objects[4] = 'Z'; //原先字符串现在数字
        for (Object object : objects) {
            System.out.println(object);
        }

    }
}
