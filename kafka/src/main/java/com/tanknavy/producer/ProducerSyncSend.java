package com.tanknavy.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Author: Alex Cheng 7/14/2020 4:40 PM
 * Future是异步的
 * send的方法返回是一个Future对象，要实现同步发送的效果，只需要调用Future对象的get方法即可
 *
 * 如果想要保存topic全局有序，使用一个分区+同步发送，保证消息有序
 *
 * A Future represents the result of an asynchronous computation. Methods are provided to
 * check if the computation is complete, to wait for its completion, and to retrieve the
 * result of the computation. The result can only be retrieved using method get when the
 * computation has completed, blocking if necessary until it is ready
 *
 */
public class ProducerSyncSend {

    public static void main(String[] args) {

        //1.创建kafka生产者的配置信息
        Properties props = new Properties(); //java.util下,可以loadXML,

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"spark3:9092,spark1:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG,3);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); //批次16k大小
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);//如果批次大小未达到，等待1ms就发送
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); //RecordAccumulator缓冲区大小

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        //2.创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        //发送数据,异步发送
        for(int i=0;i<10;i++){
            Future<RecordMetadata> future = producer.send(new ProducerRecord<>("temp", "tanknavy--" + i));
            try {
                RecordMetadata recordMetadata = future.get(); //和callback中返回的结果一样，调用Future对象的get方法会阻塞主线程
                System.out.println(recordMetadata.toString()); //返回topic-partition@offset
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        //关闭资源
        producer.close();

    }

}
