package com.tanknavy.blog.utils;

import com.tanknavy.blog.constants.Constants;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * Author: Alex Cheng 7/11/2020 11:21 PM
 * 1.创建命名空间
 * 2.表是否存在
 * 3.创建表
 *
 */
public class HBaseUtil {

    public static void createNamespace(String namespace) throws IOException {
        //1.获取connection对象
        Connection conn = ConnectionFactory.createConnection(Constants.CONFIGURATION);
        //2.获取admin对象
        Admin admin = conn.getAdmin();
        //3.构建命名空间描述器
        NamespaceDescriptor nsDescriptor = NamespaceDescriptor.create(namespace).build();
        //4.创建命名空间
        admin.createNamespace(nsDescriptor);
        //5.关闭资源
        admin.close();
        conn.close();
    }


    private static boolean isTableExist(String tableName) throws IOException {
        //1.获取connection对象
        Connection conn = ConnectionFactory.createConnection(Constants.CONFIGURATION);
        //2.获取admin对象
        Admin admin = conn.getAdmin();

        boolean exists = admin.tableExists(TableName.valueOf(tableName));

        admin.close();
        conn.close();
        return exists;
    }


    public static void createTable(String tableName, int verions, String... cfs) throws IOException {//列族可能有多个
        //1.判断是否传入了列族信息
        if(cfs.length<=0){
            System.out.println("请设置列族信息");
            return;
        }
        //2.表是否存在
        if(isTableExist(tableName)){
            System.out.println(tableName + "表已经存在！！！");
            return;
        }

        //获取connection对象
        Connection conn = ConnectionFactory.createConnection(Constants.CONFIGURATION);

        //获取admin对象
        Admin admin = conn.getAdmin();
        //创建表描述器
        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));

        //循环添加列族信息
        for (String cf : cfs) {
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);
            //设置版本
            hColumnDescriptor.setMaxVersions(verions);
            //添加列族信息
            hTableDescriptor.addFamily(hColumnDescriptor);
        }

        //创建表
        admin.createTable(hTableDescriptor);
        //关闭资源
        admin.close();
        conn.close();

    }

}
