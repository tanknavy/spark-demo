package com.tanknavy.mr02;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put; //rowkey和put,get,delete一一对应
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Author: Alex Cheng 7/11/2020 10:32 AM
 * mr读取Hbase表写出
 */

//写出到Hbase的k/v类型，如果写到NullWritable会将数据全部集中到一个reducer,这里还是使用ImmutableBytesWritable对应Hbase的rowkey
public class Fruit2Mapper extends TableMapper<ImmutableBytesWritable, Put> { //输出类型

    private String columnQualifier = null;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        columnQualifier = configuration.get("cq");
    }

    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

        //构建put对象写出
        Put put = new Put(key.get());//根据rowkey产生一个put对象

        //1.获取hbase数据，因为这里只需要一个栏位
        for (Cell cell : value.rawCells()) {
            //2.判断当前单元格是否是name列
            //if("name".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))){//防止空指针，
            if(columnQualifier.equals(Bytes.toString(CellUtil.cloneQualifier(cell)))){//防止空指针，
                //3.给put对象赋值,
                put.add(cell);
            }
        }

        //4.mapper写出,如果想做rowcounter，写出k/v(NullWritable, 1L)
        context.write(key, put);//写出k/v

    }
}
