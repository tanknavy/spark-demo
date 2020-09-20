package com.tanknavy.java_basic;

/**
 * Author: Alex Cheng 9/19/2020 11:50 PM
 */
public class SwitchDemo {
    public static void main(String[] args) {
        int i = 0;
        switch (i){
            case 0:
                System.out.println("it's zero");
                break;
            case 1:
                System.out.println("it's one");
                break;
            case 5:
                System.out.println("it's five");
                break;
            case 10:
                System.out.println("it's ten");
                break;
            default:
                System.out.println("don't know");
                break;
        }
    }
}
