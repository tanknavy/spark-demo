package com.tanknavy.blog.dao;

import com.tanknavy.blog.constants.Constants;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Alex Cheng 7/12/2020 10:42 AM
 * 1.发布微博
 * 2.删除微博
 * 3.关注用户
 * 4.取关用户
 * 5.获取用户微博详情
 * 6.获取用户的初始化页面
 */
public class HBaseDao { //发布微博

    //发布微博：新增微博内容，还要找到粉丝群，更新inbox
    public static void publishBlog(String uid, String content) throws IOException {
        //获取连接，连接池
        Connection conn = ConnectionFactory.createConnection(Constants.CONFIGURATION);

        //第一部分，操作微博内容表
        Table conTable = conn.getTable(TableName.valueOf(Constants.CONTENT_TABLE));

        //获取当前时间戳
        long ts = System.currentTimeMillis();

        //获取rowkey
        String rowKey = uid + "_" + ts;

        //创建Put对象
        Put contPut = new Put(Bytes.toBytes(rowKey));

        //给put对象赋值, 注意，里面两个content,一个是列名，一个是参数content传入的微博内容
        contPut.addColumn(Bytes.toBytes(Constants.CONTENT_TABLE_CF), Bytes.toBytes("content"), Bytes.toBytes(content));

        //执行插入数据操作
        conTable.put(contPut);


        //第二部分：操作微博收件箱表
        //.获取用户关系表对象，两个列族，一个attends,一个fans
        Table relaTable = conn.getTable(TableName.valueOf(Constants.RELATION_TABLE));
        //获取当前微博发布人的fans列族数据
        Get get = new Get(Bytes.toBytes(uid)); //get对象和rowkey一一对应
        get.addFamily(Bytes.toBytes(Constants.RELATION_TABLE_CF2)); //指定fan列族
        Result result = relaTable.get(get);

        //创建集合，用于存放微博内容表的put对象
        ArrayList<Put> inboxPuts = new ArrayList<Put>();

        for (Cell cell : result.rawCells()) {//拿到用户的粉丝

            //if (Constants.RELATION_TABLE_CF2.equals(Bytes.toString(CellUtil.cloneFamily(cell)))){ //如果列族是fans}
            //构建微博收件箱的put对象
            Put inboxPut = new Put(CellUtil.cloneQualifier(cell)); //关系表中的fans列族的列就是uid,这个uid就是收件表中rowkey
            //收件箱中内容
            inboxPut.addColumn(Bytes.toBytes(Constants.INBOX_TABLE_CF), Bytes.toBytes(uid), Bytes.toBytes(rowKey));//收件内容使用rowKey
            //将收件箱表的put对象存入结合
            inboxPuts.add(inboxPut);
        }

        //如果有粉丝
        if(inboxPuts.size() > 0 ){
            //获取收件箱表对象
            Table inboxTable = conn.getTable(TableName.valueOf(Constants.INBOX_TABLE));

            //执行收件箱表数据批量插入
            inboxTable.put(inboxPuts);

            //关闭收件箱表
            inboxTable.close();
        }

        //关闭资源
        relaTable.close();
        conTable.close();
        conn.close();

    }


    //前台页面点击删除微博
    public static void deleteBlog(String uid, String rowkey){

    }


    //关注用户
    public static void addAttends(String uid, String... attends) throws IOException { //可以一次关注任意个用户

        //前台校验attends,后台也可以
        if(attends.length <= 0){
            //给前台一个json消息，这个方法封装一个message而不是void
            System.out.println("please choose the person you interest");
            return;
        }

        //获取连接，连接池
        Connection conn = ConnectionFactory.createConnection(Constants.CONFIGURATION);

        //第一部分，添加关注用户
        Table relaTable = conn.getTable(TableName.valueOf(Constants.RELATION_TABLE));
        //创建一个集合，用于存放用户关系表的Put对象
        ArrayList<Put> relaPuts = new ArrayList<>();

        //创建操作者的Put对象
        Put uidPut = new Put(Bytes.toBytes(uid));

        //1个用户关注3个用户时，产生4个put对象
        //循环创建被关注者的Put对象
        for (String attend : attends) {

            uidPut.addColumn(Bytes.toBytes(Constants.RELATION_TABLE_CF1), Bytes.toBytes(attend), null);//内容可以为空，也可以和列明一样
            Put attPut = new Put(Bytes.toBytes(attend)); //关注者
            attPut.addColumn(Bytes.toBytes(Constants.RELATION_TABLE_CF2), Bytes.toBytes(uid), null);
            relaPuts.add(attPut);
        }
        relaPuts.add(uidPut); //关系表中全部put准备完成
        relaTable.put(relaPuts); //批量插入


        //第二部分，被关注者最新微博内容，即更新收件箱表，
        //或者微博内容表对象
        Table contTable = conn.getTable(TableName.valueOf(Constants.CONTENT_TABLE));

        //创建收件箱表的put对象，需要从内容表中取得内容
        Put inboxPut = new Put(Bytes.toBytes(uid));

        //如何或者单个关注者的近期微博？ get必须指定rowkey,但是不知道timestamp, 所以使用scan，使用startRow, endRow? uid_比uid_xxx要小 -> uid|比uid_xxx要大,ascii编码中|是124位
        //循环attend,获取每个关注者的近期发布的微博，
        for (String attend : attends) {
            Scan scan = new Scan(Bytes.toBytes(attend + "_"), Bytes.toBytes(attend + "|")); // 对照rowkey命名规则startRow->endRow，得到用户全部微博
            ResultScanner resultScanner = contTable.getScanner(scan);//scan后的结果会自动排序，先按照rowkey,再按照cf,再col,在timestamp

            //将要写入到收件箱表时，时间戳是系统传入的，都一样，结果只能显示一条
            long ts = System.currentTimeMillis();//客户端的时间戳
            System.out.println("------timestamp on  add attends:" + ts);

            //对获取的值遍历,为空拿到最近的微博的rowkey
            for (Result result : resultScanner) { //遍历会按照rowkey从小到大，在cell中按照版本version
                //给收件表的put对象赋值，只需要最后3条数据，怎么操作，ResultScanner是一个迭代器，不能反转
                //思路一：将微博的rowkey设计时，13个9-timestamp实现按时间戳倒排，这样可以取前3个最小的，就是最近3个
                //思路二：
                inboxPut.addColumn(Bytes.toBytes(Constants.INBOX_TABLE_CF), Bytes.toBytes(attend), ts++, result.getRow()); //批量插入时，时间戳可能一样，手动指定ts++
                //Thread.sleep(1);//这样让系统添加不同时间戳可以吗？不行，因为client传入到server端时，server端发现没有时间戳会统一添加，结果时间戳又一样
            }
        }


        //判断put对象的内容是否为空,避免用户微博为空
        if(!inboxPut.isEmpty()){
            Table inboxTable = conn.getTable(TableName.valueOf(Constants.INBOX_TABLE));
            //批量插入
            inboxTable.put(inboxPut);
            //关闭收件箱表资源
            inboxTable.close();
        }

        //关闭资源
        contTable.close();
        relaTable.close();
        conn.close();

    }


    //取关用户
    public static void deleteAttends(String uid, String... attends) throws IOException {
        if(attends.length <=0){
            System.out.println("请传入取关者");
            return;
        }

        //获取连接，连接池
        Connection conn = ConnectionFactory.createConnection(Constants.CONFIGURATION);
        //操作用户关系表
        Table relaTable = conn.getTable(TableName.valueOf(Constants.RELATION_TABLE));
        //创建集合，用于存放关系表的Delete对象
        ArrayList<Delete> relaDeletes = new ArrayList<>();

        //创建操作者的delete对象
        Delete uidDelete = new Delete(Bytes.toBytes(uid));
        //uidDelete.addFamily(Bytes.toBytes(Constants.RELATION_TABLE_CF1));
        //创建收件表的delete对象
        Delete inboxDeletes = new Delete(Bytes.toBytes(uid));

        //迭代删除
        for (String attend : attends) {
            //取消关注谁,
            uidDelete.addColumns(Bytes.toBytes(Constants.RELATION_TABLE_CF1), Bytes.toBytes(attend)); //Columns对比Column是多个版本

            //取消关注谁的收件箱消息
            inboxDeletes.addColumns(Bytes.toBytes(Constants.INBOX_TABLE_CF), Bytes.toBytes(attend));//删除这个用户该列全部信息，不想再看他的消息

            //谁的粉丝走掉
            Delete attDelete = new Delete(Bytes.toBytes(attend));
            attDelete.addColumns(Bytes.toBytes(Constants.RELATION_TABLE_CF2), Bytes.toBytes(uid));//赋值

            //添加到结合
            relaDeletes.add(attDelete);
        }
        relaDeletes.add(uidDelete);

        //可以不用判断，因为前台会校验，有过关注才开一删除
        if(!relaDeletes.isEmpty()){
            relaTable.delete(relaDeletes);
        }

        //删除收件表
        Table inboxTable = conn.getTable(TableName.valueOf(Constants.INBOX_TABLE));
        //执行删除
        if(!inboxDeletes.isEmpty()){
            inboxTable.delete(inboxDeletes);
        }

        //关闭资源
        relaTable.close();
        inboxTable.close();
        conn.close();

    }


    //获取初始化页面，返回一个用户收件箱消息
    public static void getInit(String uid) throws IOException {
        //获取连接，连接池
        Connection conn = ConnectionFactory.createConnection(Constants.CONFIGURATION);

        //收件箱表
        Table inboxTable = conn.getTable(TableName.valueOf(Constants.INBOX_TABLE));
        //微博内容表
        Table contTable = conn.getTable(TableName.valueOf(Constants.CONTENT_TABLE));

        //获取被关注者的全部最新微博，注意：在inboxTable中，微博内容储存的是rowkey,所以拿到rowkey还要去content表中查
        //收件箱的get对象，并获取数据，
        Get inboxGet = new Get(Bytes.toBytes(uid));
        inboxGet.setMaxVersions(2);
        inboxGet.addFamily(Bytes.toBytes(Constants.INBOX_TABLE_CF));
        Result inboxResult = inboxTable.get(inboxGet);

        ArrayList<byte[]> contRowkeys = new ArrayList<>();

        //循环，每个栏位，每个栏位下面近3个版本
        for (Cell cell : inboxResult.rawCells()) {
            //构建微博内容Get对象
            Get contGet = new Get(CellUtil.cloneValue(cell));//单元格有多个版本
            //获取微博内容
            Result contResult = contTable.get(contGet);
            //解析内容
            for (Cell contCell : contResult.rawCells()) {
                //String content = Bytes.toString(CellUtil.cloneValue(cell));
                //5每个cell的数据
                System.out.println("RK:" + Bytes.toString(CellUtil.cloneRow(contCell))
                        + ", CF:" + Bytes.toString(CellUtil.cloneFamily(contCell))
                        + ", Qualifier:" + Bytes.toString(CellUtil.cloneQualifier(contCell))
                        + ", Value:" + Bytes.toString(CellUtil.cloneValue(contCell))
                        + ", TimeStamp:" + cell.getTimestamp()
                );
            }
        }

        //关闭资源
        inboxTable.close();
        contTable.close();
        conn.close();

    }



    //查看某人全部微博，使用过滤器RowFilter，内置SubstringComparator
    public static void getBlog(String uid) throws IOException {
        //获取连接，连接池
        Connection conn = ConnectionFactory.createConnection(Constants.CONFIGURATION);

        //微博内容表
        Table contTable = conn.getTable(TableName.valueOf(Constants.CONTENT_TABLE));

        //scan对象
        Scan scan = new Scan();

        //构建过滤器, 从Filter上ctrl+H找到Filter这个抽象类的下面一个合适的实现类
        //SubString过滤器尝试从rowkey中抽取uid_字符串
        RowFilter rowFilter = new RowFilter(CompareOperator.EQUAL, new SubstringComparator(uid+ "_")); //rowkey命名规则:uid_xxx
        //RowFilter rowFilterLow = new RowFilter(CompareOperator.GREATER, new SubstringComparator(uid+ "_")); //rowkey命名规则:uid_xxx
        //RowFilter rowFilterUp = new RowFilter(CompareOperator.LESS, new SubstringComparator(uid+ "|"));
        //FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, Arrays.asList(rowFilterLow, rowFilterUp));//构造器集合

        scan.setFilter(rowFilter);

        //获取全部数据
        ResultScanner resultScanner = contTable.getScanner(scan);

        //解析数据并打印
        for (Result result : resultScanner) { //每条记录
            for (Cell cell : result.rawCells()) {//每条记录中的单元格
                System.out.println("RK:" + Bytes.toString(CellUtil.cloneRow(cell))
                        + ", CF:" + Bytes.toString(CellUtil.cloneFamily(cell))
                        + ", Qualifier:" + Bytes.toString(CellUtil.cloneQualifier(cell))
                        + ", Value:" + Bytes.toString(CellUtil.cloneValue(cell))
                        + ", TimeStamp:" + cell.getTimestamp()
                );
            }
        }

        //关闭资源
        contTable.close();
        conn.close();


    }




}
