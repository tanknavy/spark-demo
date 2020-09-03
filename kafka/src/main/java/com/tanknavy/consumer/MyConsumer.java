package com.tanknavy.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * Author: Alex Cheng 7/14/2020 5:39 PM
 */
public class MyConsumer {
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "spark3:9092,spark1:9091");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);//开启自动提交offset,可以手动提交,
        //手动提交offset的方法有两种，commitSync和commitAsync，都会将本次poll的批量数据中最高偏移量提交，
        // 一个阻塞当前线程，一直到成功，并且会自动重试，一个不会阻塞，也没有失败重试机制，所以有可能提交失败
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");//自动提交offset的延时，一秒提交一次
        //ctrl+n 查一下
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        //消费者组,在console中有默认的随机分配，
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bigdata");
        //如果想从最初offset开始消费，earliest, latest两个可选，默认latest, 注意这时需要更改group！满足两个条件
        //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");//当没有初始化的offset时(比如消费者组第一次)，或者当前offset已经不存在，否则不能reset

        //创建消费者
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("temp"));//订阅集合，Arrays.asList()

        while (true) { //不停的拉取
            //批量获取数据
            ConsumerRecords<Object, Object> consumerRecords = consumer.poll(100);//kafka消费者是拉取模式，如果没有拉取到，等待这里指定的时间

            //解析并打印consumerRecords
            for (ConsumerRecord<Object, Object> consumerRecord : consumerRecords) {

                System.out.println("主题/分区/偏移/键/值: ---"
                        + " 主题:" + consumerRecord.topic()
                        + " 分区:" + consumerRecord.partition()
                        + " 偏移:" + consumerRecord.offset()
                        + " 键:" + consumerRecord.key()
                        + " 值:" + consumerRecord.value());
            }
        }

        //关闭连接
        //consumer.close();

    }
}
