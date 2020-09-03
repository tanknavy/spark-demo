package com.tanknavy.mr01;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * Author: Alex Cheng 7/10/2020 10:04 PM
 * mr读取文件写入到Hbase表中
 * 文件格式：1001 apple red
 *
 */

//如果在mapper中输出k为NullWritable类型将集中到一个reducer，TableReducer自定义好了keyOut(put/get/delete)
//这里reducer端输出键不需要就写NullWritable
public class FruitReducer extends TableReducer<LongWritable, Text, NullWritable> {
    String cf = null;
    String cq1 = null;
    String cq2 = null;

    //可以从context读取main中设定的参数
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();//在main方法中configuration.set("cf","cf"),这里configuration.get("cf")得到
        cf = configuration.get("cf");//从main读设置这里的cf,cq
        cq1 = configuration.get("cq1");//从main读设置这里的cf,cq
        cq2 = configuration.get("cq2");//从main读设置这里的cf,cq
    }

    //reduce方法
    @Override
    protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //super.reduce(key, values, context);

        //1.遍历values
        for (Text value : values) {
            //2.获取每一行数据
            String[] fields = value.toString().split("\t");//1001 apple red
            //3.构建Put对象
            Put put = new Put(Bytes.toBytes(fields[0]));//rowkey
            //4.给put对象赋值,//想通过main方法传入参数,实现上面的setup
            //put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(fields[1]));
            //put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("color"), Bytes.toBytes(fields[2]));
            //改为从driver端读取
            System.out.println("----------" + cf + "-------------");
            put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cq1), Bytes.toBytes(fields[1]));
            put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cq2), Bytes.toBytes(fields[2]));

            //5.reduce写出数据
            context.write(NullWritable.get(), put);
        }
    }

}
