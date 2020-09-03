package com.tanknavy.blog.test;

import com.tanknavy.blog.constants.Constants;
import com.tanknavy.blog.dao.HBaseDao;
import com.tanknavy.blog.utils.HBaseUtil;

import java.io.IOException;

/**
 * Author: Alex Cheng 7/12/2020 6:06 PM
 */
public class TestWeiBo {

    public static void init(){
        try {
            //创建命名空间
            //HBaseUtil.createNamespace(Constants.NAMESPACE);

            //创建微博内容表
            HBaseUtil.createTable(Constants.CONTENT_TABLE, Constants.CONTENT_TABLE_VERSIONS, Constants.CONTENT_TABLE_CF);

            //创建用户关系表
            HBaseUtil.createTable(Constants.RELATION_TABLE, Constants.RELATION_TABLE_VERSIONS, Constants.RELATION_TABLE_CF1,
                    Constants.RELATION_TABLE_CF2);

            //创建收件箱表
            HBaseUtil.createTable(Constants.INBOX_TABLE, Constants.INBOX_TABLE_VERSIONS, Constants.INBOX_TABLE_CF);

        }catch (IOException e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        //第一次初始化
        init();

        //1001发布微博
        HBaseDao.publishBlog("1001", "aa下课啦！");
        //Thread.sleep(10);

        //1002关注1001和1003
        HBaseDao.addAttends("1002", "1001","1003");

        //获取1002初始化页面
        HBaseDao.getInit("1002");

        System.out.println("------------111-------------");

        //1003发布3条微博,同时1001发布2条微博
        HBaseDao.publishBlog("1003", "炎热的周六--不应该出现");//只设定两个版本，所以这个最旧的应该不会出现
        Thread.sleep(10);
        HBaseDao.publishBlog("1003", "完美的周日");
        Thread.sleep(10);
        HBaseDao.publishBlog("1003", "课程结束啦");
        Thread.sleep(10);
        //HBaseDao.publishBlog("1001", "好像有个小bug");
        //Thread.sleep(10);

        HBaseDao.publishBlog("1001", "bb新鲜的开始");
        Thread.sleep(10);
        HBaseDao.publishBlog("1001", "cc百万美元富翁");
        Thread.sleep(10);

        //获取1002初始化页面
        HBaseDao.getInit("1002");
        System.out.println("------------222-------------");
        //1002取关1003，
        HBaseDao.deleteAttends("1002", "1003");

        //获取1002初始化页面
        HBaseDao.getInit("1002");
        System.out.println("------------333-------------");

        //1002再次关注1003
        HBaseDao.addAttends("1002", "1001","1003");

        //获取1002初始化页面
        HBaseDao.getInit("1002");
        System.out.println("------------444-------------");

        //获取1001微博详情
        HBaseDao.getBlog("1001");

    }

}

/*

disable "weibo:inbox"
drop  "weibo:inbox"
disable "weibo:relation"
drop "weibo:relation"
disable "weibo:content"
drop  "weibo:content"
list

 */