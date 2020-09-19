package com.tanknavy.collection_java;

import java.util.HashSet;

/**
 * Author: Alex Cheng 9/18/2020 4:46 PM
 */
public class JavaHashSet {
    public static void main(String[] args) {

        //不重复，无序
        HashSet<String> hs = new HashSet<String>();
        hs.add("adm");
        hs.add("bob");
        hs.add("adm");
        hs.add("david");

        System.out.println(hs);

    }
}
