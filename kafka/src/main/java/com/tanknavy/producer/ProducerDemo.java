package com.tanknavy.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.rmi.PortableRemoteObject;
import java.util.Properties;

/**
 * Author: Alex Cheng 7/14/2020 10:59 AM
 */
public class ProducerDemo {
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
            producer.send(new ProducerRecord<>("temp", "tanknavy--" +i));
        }

        //关闭资源
        producer.close();//上述没到16k或者没到1毫秒，如果不加close,数据不会发送，因为产生数据很快，不到1ms

    }
}
