package com.tanknavy.idea;

/**
 * Author: Alex Cheng 11/17/2020 6:29 PM
 */
public class Exam01 {
    public static void main(String[] args) {
        System.out.println(str2int("3"));
    }

    public static int str2int(String str){
        int i = 0;
        try {
            i = Integer.parseInt(str); //代码块范围
        }catch (Exception e){
            System.out.println(e);
        }
        return i;

    }
}
