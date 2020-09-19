package com.tanknavy.collection_java;

import java.util.HashMap;

/**
 * Author: Alex Cheng 9/18/2020 1:34 PM
 */
public class JavaHashMap {
    public static void main(String[] args) {
        HashMap<String, Integer> hm = new HashMap<>(); //无序
        hm.put("no1", 100);
        hm.put("no2", 200);
        hm.put("no3", 300);
        hm.put("no1", 500);

        System.out.println(hm);
    }
}
