package com.tanknavy.collection_java;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Alex Cheng 9/17/2020 5:03 PM
 */
//为了说明scala的inmutable和mutable
public class MutableCollection {
    public static void main(String[] args) {
        int[] nums = new int[3]; //长度不可以变
        nums[0] = 1;
        nums[1] = 2;
        nums[2] = 'a'; //char可以直接转int,对应ascii编码
        System.out.println(Arrays.toString(nums));
        //nums[3] = 3;//数组越界

        ArrayList<Integer> list = new ArrayList<Integer>();//动态增长
        list.add(11);
        System.out.println(list.hashCode());
        list.add(22);
        System.out.println(list.hashCode());//hashCode有变化，
    }
}
