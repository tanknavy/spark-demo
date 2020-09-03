package com.tanknavy.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * Author: Alex Cheng 7/14/2020 8:28 PM
 */
public class CounterInterceptor implements ProducerInterceptor<String,String> {

    int success;
    int fail;

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        //return null;
        return producerRecord;//直接返回，这里不需要过滤数据
    }

    @Override //ack，类型callback，成功返回metadata, 失败返回e
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if(recordMetadata != null){
            success ++;
        }else {
            fail ++;
        }
    }

    @Override //关掉producer时，输出成功失败的统计结果
    public void close() {
        System.out.printf("消息成功发送:%d条, 发送失败:%d条",success,fail);
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
