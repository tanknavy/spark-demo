package com.tanknavy.json;

import com.google.gson.Gson;
import org.junit.Test;

import java.util.Map;

/**
 * Author: Alex Cheng 7/16/2020 1:07 PM
 * json字符串
 * Javascript里面JSON对象的parse和stringify方法实现object<->json的互相转换
 */
public class MyTest {

    @Test
    public void testJson() {
        //创建一个json字符串
        String json = "{\"name\":\"孙悟空\",\"age\":19,\"gender\":\"男\"}";

        //在java中默认不支持JSON解析，使用json-lib, jackson, gson, fastjson等的
        System.out.println(json);

        //使用Gson转map
        Gson gson = new Gson();
        Map map = gson.fromJson(json, Map.class);//将json字符串转成Map对象
        System.out.println(map);

        //使用Gson转object
        Student stu = gson.fromJson(json, Student.class);
        System.out.println(stu);

    }

    @Test
    public void testObj(){
        Student stu = new Student("猪八戒", 28, "男");

        Gson gson = new Gson();
        String json = gson.toJson(stu);

        System.out.println(json);
    }

}


