package com.tanknavy.kafka.basic;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemoGroups {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ConsumerDemoGroups.class.getName());

        String bootstrapServers = "127.0.0.1:9092";
        String topic = "first_topic";
        String groupId = "my-fourth-application";


        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest"); //earliest/latest/none

        //create consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        // subscribe consumer to topic
        consumer.subscribe(Collections.singletonList(topic));
        //consumer.subscribe(Arrays.asList("first_topic","second_topic"));

        // poll for new data
        while(true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String,String> record: records){
                logger.info("key: " + record.key() +",value: " + record.value());
                logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
            }
        }


    }
}
