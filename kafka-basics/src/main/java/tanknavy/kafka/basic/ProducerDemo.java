package com.tanknavy.kafka.basic;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;


public class ProducerDemo {
    public static void main(String[] args) {
        String bootstrapServers = "127.0.0.1:9092";

        // create kafka producer properties
        Properties properties = new Properties();
        //properties.setProperty("bootstrap.servers","localhost:9092");
        // 使用这用方法设置kafka参数名称
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        // create the producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        // create a producer record
        ProducerRecord<String,String> record = new ProducerRecord<String,String>("first_topic","hello world from kafka java");

        // send data -asynchronous
        producer.send(record);
        producer.flush();
        producer.close();
    }
}
