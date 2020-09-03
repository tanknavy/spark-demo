package com.tanknavy.mongo;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.tanknavy.json.Student;
import org.bson.Document;
import org.junit.Test;

import java.util.HashMap;

/**
 * Author: Alex Cheng 7/16/2020 9:57 PM
 */

public class TestMongo {

    public void client(){

    }

    @Test
    public void crud(){
        //创建client
        MongoClient client = new MongoClient("127.0.0.1",27017);//使用localhost会出错

        //连接指定数据库
        MongoDatabase userDB = client.getDatabase("userDB");

        //获取指定集合对象
        MongoCollection<Document> users = userDB.getCollection("users");

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "元帅");
        map.put("age", 28);
        map.put("hobby", "美食，美女");

        //创建一个文档
        Document docu = new Document(map);
        docu.append("对象", "月亮姐姐");

        //向users集合中插入一个文档
        users.insertOne(docu);

        //关闭资源
        client.close();
    }

    @Test
    public void testInser(){
        MongoClient client = new MongoClient();
        MongoDatabase userDB = client.getDatabase("userDB");
        MongoCollection<Document> students = userDB.getCollection("students");

        //创建java对象
        Student stu = new Student("丁三石", 41, "男");

        //java对象转换成json串
        Gson gson = new Gson();
        String json = gson.toJson(stu);

        //将json转换成document
        Document doc = Document.parse(json);

        //将对象插入db
        students.insertOne(doc);

        //关闭资源
        client.close();
    }

    @Test
    public void queryOne(){
        MongoClient client = new MongoClient();
        MongoDatabase userDB = client.getDatabase("userDB");
        MongoCollection<Document> students = userDB.getCollection("students");

        Document docu = students.find().first();
        System.out.println(docu.toString());

        Gson gson = new Gson();
        Student student = gson.fromJson(docu.toJson(), Student.class);
        System.out.println(student);
    }

    @Test
    public void queryMany(){
        MongoClient client = new MongoClient();
        MongoDatabase userDB = client.getDatabase("userDB");
        MongoCollection<Document> students = userDB.getCollection("students");

        FindIterable<Document> docus = students.find().limit(2).skip(1); //limit和skip实现分页


        for (Document document : docus) {
            System.out.println(document.toJson());
        }

        //Filters实现过滤，返回bson
        Document first = students.find(Filters.eq("name", "有钱银")).first();
        System.out.println("偶是低调的："+first.toJson());

    }

    @Test
    public void remove(){
        MongoClient client = new MongoClient();
        MongoDatabase userDB = client.getDatabase("userDB");
        MongoCollection<Document> students = userDB.getCollection("students");

        students.deleteOne(Filters.eq("name", "丁三石"));//和查询条件一样

    }

    @Test
    public void update(){
        MongoClient client = new MongoClient();
        MongoDatabase userDB = client.getDatabase("userDB");
        MongoCollection<Document> students = userDB.getCollection("students");

        //db.collection.updateOne({"key":"value},{$set:{"newKey","value"}}) //对于javascript的这种内嵌文档的对象
        students.updateOne(Filters.eq("name","丁三石"), new Document("$set", new Document("资产","$360M")));//内嵌文档

    }



}
