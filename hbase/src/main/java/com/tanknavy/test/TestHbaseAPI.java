package com.tanknavy.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import scala.util.control.Exception;


import java.io.IOException;

/**
 * Author: Alex Cheng 7/9/2020 10:47 PM
 * DDL:
 * 1）判断表是否存在
 * 2）创建命名空间
 * 3）创建表，删除表
 * DML：HBase里面对象都是字节数组类型，util包下Bytes工具类提供方法
 * 4）插入数据
 * 5 查询数据(get)
 * 6)查数据(scan)
 * 7) 删除数据
 */


//HBase 1.3.1版本
public class TestHbaseAPI {

    //连接静态代买块
    public static Connection conn = null;
    private static Admin admin = null; //DDL操作都是需要admin对象

    static { //类加载时就执行
        try {
            //1.获取配置文件信息
            //HBaseConfiguration configuration = new HBaseConfiguration(); //过时
            Configuration configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum", "spark1,spark3"); //找hbase-site.xml中配置

            //2.获取connection和admin对象
            //HBaseAdmin admin = new HBaseAdmin(configuration);//过时
            conn = ConnectionFactory.createConnection(configuration);
            admin = conn.getAdmin();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //关闭资源
    public static void close(){

        if(admin != null){
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(conn != null){
            try {
                conn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //1)表是否存在
    public static boolean isTableExist(String tableName) throws IOException {

        //3.判断表是否存在
        //boolean exists = admin.tableExists(tableName);//过时
        boolean exists = admin.tableExists(TableName.valueOf(tableName)); //中间是TableName对象

        //4.关闭连接
        //admin.close();//统一资源

        //5.返回结果
        return exists;

    }

    //2.创建表, namespace:tableName, tableName will create in default namespace
    public static void createTable(String tableName, String... cfs) throws IOException {
        //1.是否存在列族信息
        if(cfs.length <=0){
            System.out.println("请设置列族信息");
        }
        //2.表是否存在
        if(isTableExist(tableName)){
            System.out.println(tableName + "表已经存在");
            return;
        }
        //3.创建表描述器,2.0以后TableDescriptorBuiler
        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
        //4.循环添加列族信息
        for (String cf : cfs) {
            //5.创建列族信息
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);
            //hColumnDescriptor.setMaxVersions(3); //列族版本设置
            hTableDescriptor.addFamily(hColumnDescriptor);
        }

        //6.创建表
        admin.createTable(hTableDescriptor);
        
    }

    //3.删除表
    public static void dropTable(String tableName) throws IOException {
        //1.判断表是否存在
        if(!isTableExist(tableName)){
            System.out.println(tableName + "表不存在!");
            return;
        }

        //2.表下线
        admin.disableTable(TableName.valueOf(tableName));
        admin.deleteTable(TableName.valueOf(tableName));
    }


    //4.创建命名空间
    public static void createNameSpace(String ns) {

        try {
            admin.createNamespace(NamespaceDescriptor.create(ns).build());
        }catch (NamespaceExistException e){ //准确抓取异常
            System.out.println(ns + " namespace已经存在");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("哈哈哈，命名空间已经存在！ 主动处理掉异常，继续走到这里");
    }


    //5.表内插入资源
    public static void putData(String tableName, String rowKey, String cf, String cn, String value) throws IOException {
        //1.获取表对象
        Table table = conn.getTable(TableName.valueOf(tableName));
        //2.创建Put对象
        Put put = new Put(Bytes.toBytes(rowKey));
        //3.给put对象赋值
        put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cn), Bytes.toBytes(value));
        //4.插入数
        table.put(put);
        //5.关闭表连接
        table.close();

    }

    //6.获取数据
    public static void getData(String tabName, String rowKey, String cf, String cq) throws IOException {
        //1.获取表对象
        Table table = conn.getTable(TableName.valueOf(tabName));
        //2.创建get对象
        Get get = new Get(Bytes.toBytes(rowKey));
        //2.1指定获取的列族
        //get.addFamily(Bytes.toBytes(cf));
        //2.2.指定列
        get.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cq));
        //2.3.获取多个版本数据
        get.setMaxVersions(5);


        //3.获取数据
        Result result = table.get(get);
        //4.解析result并打印
        for (Cell cell : result.rawCells()) {
           //5每个cell的数据
            System.out.println("CF:" + Bytes.toString(CellUtil.cloneFamily(cell))
                            + ", Qualifier:" + Bytes.toString(CellUtil.cloneQualifier(cell))
                            + ", Value:" + Bytes.toString(CellUtil.cloneValue(cell))
                            + ", TimeStamp:" + cell.getTimestamp()
                            + ", TypeByte:" + String.valueOf(cell.getTypeByte())
            );
        }
        //5.关闭表连接
        table.close();

    }


    //7.查查数据(scan),打印
    public static void scanTable(String tabName) throws IOException {
        //1.获取表对象
        Table table = conn.getTable(TableName.valueOf(tabName));
        //2.构建scan对象
        Scan scan = new Scan();//空参，可以使用范围和过滤器
        //Scan scan = new Scan(Bytes.toBytes("1000"),Bytes.toBytes("1002"));//空参，可以使用范围和过滤器
        //3.扫描表
        ResultScanner resultScanner = table.getScanner(scan);//返回ResultScanner，类似一个迭代器，一批一批的返回对象
        //4.解析结果
        for (Result result : resultScanner) {
            //System.out.println("rowKey:" + Bytes.toString(result.getRow()));
            //5.解析result并打印
            for (Cell cell : result.rawCells()) {
                //5每个cell的数据
                System.out.println("RowKey: " + Bytes.toString(CellUtil.cloneRow(cell))
                        + ", CF:" + Bytes.toString(CellUtil.cloneFamily(cell))
                        + ", Qualifier:" + Bytes.toString(CellUtil.cloneQualifier(cell))
                        + ", Value:" + Bytes.toString(CellUtil.cloneValue(cell))
                        + ", TimeStamp:" + cell.getTimestamp()
                       // + ", TypeByte:" + String.valueOf(cell.getTypeByte())
                );
            }
        }
        //关闭表资源
        table.close();

    }

    //8.删除数据,因为timestamp有点复杂
    public static void deleteData(String tabname, String rowKey, String cf, String cq) throws IOException {
        //1.获取表对象
        Table table = conn.getTable(TableName.valueOf(tabname));

        //2.构建删除对象，指定rowKey，如果传入timestamp，删除时只删除<=时间戳的数据
        Delete delete = new Delete(Bytes.toBytes(rowKey)); //只传rowKey会删除整个行
        //2.1设置删除的列,尽量使用家s的版本
        //delete.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cq));//慎用: 只删除栏位最新版本,特别小心，在多版本情况下，修改再删除后可能返会旧版本数据！！！
        delete.addColumns(Bytes.toBytes(cf), Bytes.toBytes(cq));//删除栏位所有版本，如果传入timestamp，只删这个时间戳的
        //2.2删除指定的列族
        //delete.addFamily(Bytes.toBytes(cf));

        //3.执行删除操作
        table.delete(delete);


        //关闭连接
    }


    //main方法
    public static void main(String[] args) throws IOException {

        //1.测试表是否存在
        //System.out.println(isTableExist("stu2"));

        //2.创建表测试
        //createTable("stu2", "info","info2");
        //System.out.println(isTableExist("stu2"));

        //3.删除表测试
        //dropTable("stu2");
        //System.out.println(isTableExist("stu2"));

        //4.创建namespace,类似mysql的database
        //createNameSpace("bigdata");

        //5.创建数据测试
        //putData("stu", "1006", "info", "name", "frankie");
        //putData("stu", "1006", "info", "age", "28");
        //putData("stu", "1006", "info2", "addr", "Beijing");
        putData("stu", "1007", "info", "name", "giggs");
        putData("stu", "1007", "info", "name", "jack");


        //6.获取单行数据测试
        //getData("stu", "1006", "info", "name");

        //7.scan获取多行数据测试
        scanTable("stu");

        //8.删除数据测试
        deleteData("stu", "1007", "info", "name");
        getData("stu","1007", "info", "name");

        close();//统一关闭资源
    }

}
