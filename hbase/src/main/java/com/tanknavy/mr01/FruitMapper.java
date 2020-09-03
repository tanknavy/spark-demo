package com.tanknavy.mr01;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Author: Alex Cheng 7/10/2020 10:04 PM
 */
public class FruitMapper extends Mapper<LongWritable, Text, LongWritable,Text> {//不做改变

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //super.map(key, value, context);
        System.out.println("-----------------"+value.toString());
        context.write(key, value);//文件格式：1001    apple   red
    }
}
