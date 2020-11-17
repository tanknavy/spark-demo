package com.tanknavy.kafka.basic;


import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;



public class ProducerDemoWithCallback {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);

        String bootstrapServers = "127.0.0.1:9092";

        // create kafka producer properties
        Properties properties = new Properties();
        //properties.setProperty("bootstrap.servers","localhost:9092");
        // 使用这用方法设置kafka参数名称
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer",StringSerializer.class.getName());

        // create the producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        for(int i=0;i<10;i++) {
            // create a producer record
            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>("first_topic", "ProducerDemoWithCallback" + Integer.toString(i));

            // send data -asynchronous
            //producer.send(record);
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    // a record is successfuly sent or an exception is thrown
                    if (e == null) {
                        logger.info("Received new metadata. \n" +
                                " Topic:" + metadata.topic() +
                                " Partition:" + metadata.partition() +
                                " Offset:" + metadata.offset() +
                                " Timestamp:" + metadata.timestamp());
                    } else {
                        logger.error("error while producing", e);
                    }
                }
            });
        }
        producer.flush();
        producer.close();
    }
}

