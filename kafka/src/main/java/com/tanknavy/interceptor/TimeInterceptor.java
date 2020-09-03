package com.tanknavy.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * Author: Alex Cheng 7/14/2020 8:18 PM
 * 需求：消息前面加时间戳
 */
public class TimeInterceptor implements ProducerInterceptor<String, String> {

    @Override
    public void configure(Map<String, ?> map) {

    }

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {

        //取出数据
        String value = record.value();
        //创建新对象
        return new ProducerRecord<>(record.topic(), record.partition(), record.key(), System.currentTimeMillis() + "," + value);

    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    @Override
    public void close() {

    }


}
