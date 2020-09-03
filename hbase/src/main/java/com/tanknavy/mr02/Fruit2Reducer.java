package com.tanknavy.mr02;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;

import java.io.IOException;

/**
 * Author: Alex Cheng 7/11/2020 10:32 AM
 */
public class Fruit2Reducer extends TableReducer<ImmutableBytesWritable, Put, NullWritable> {//写到表里

    @Override
    protected void reduce(ImmutableBytesWritable key, Iterable<Put> values, Context context) throws IOException, InterruptedException {
        //遍历写出
        for (Put put : values) {
            context.write(NullWritable.get(), put);//直接写出
        }
    }
}
