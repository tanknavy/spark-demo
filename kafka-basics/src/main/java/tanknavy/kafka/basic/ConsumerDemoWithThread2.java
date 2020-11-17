package com.tanknavy.kafka.basic;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerDemoWithThread2 {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(com.tanknavy.kafka.basic.ConsumerDemoWithThread.class.getName());

        String bootstrapServers = "127.0.0.1:9092";
        String topic = "first_topic";
        String groupId = "my-fifth-application";
        CountDownLatch latch = new CountDownLatch(1); //主线程只需要关注一个线程

        //一种方法是ConsumerThread变成静态
        // 第二种方法呢?
        Runnable myConsumer = new ConsumerThread(latch,bootstrapServers,groupId,topic);
        Thread consumerThread = new Thread(myConsumer);
        logger.info("consumer thread is starting");
        consumerThread.start();

        //添加shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread( () ->{
            logger.info("Caught shutdown hook");
            ((ConsumerThread) myConsumer).shutdown(); //cast

            try {
                latch.await();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            logger.info("Application has exited");
        }
        ));


        try {
            latch.await(); //当前线程等待,一定要写，不然这里的主线程结束后退出
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            logger.info("Application is closing");
        }
        //myConsumer.run();
        //create consumer
        //KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        // subscribe consumer to topic
        //consumer.subscribe(Collections.singletonList(topic));
        //consumer.subscribe(Arrays.asList("first_topic","second_topic"));

        // poll for new data


    }



    public static class ConsumerThread implements Runnable{ //一种方法是ConsumerThread变成静态
        //public class ConsumerThread implements Runnable{

        private CountDownLatch latch;
        private KafkaConsumer<String,String> consumer;
        private Logger logger = LoggerFactory.getLogger(ConsumerThread.class.getName());
        //private String topic;

        public ConsumerThread(CountDownLatch latch, String bootstrapServers, String groupId, String topic ){
            this.latch = latch;
            //this.topic = topic;
            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest"); //earliest/latest/none

            consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(Collections.singletonList(topic));
        }

        @Override
        public void run() {

            try {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                    for (ConsumerRecord<String, String> record : records) {
                        logger.info("key: " + record.key() + ",value: " + record.value());
                        logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
                    }
                }
            } catch (WakeupException e){
                logger.info("Received shutdown signal");
            } finally { // consumer退出并通知主程序
                consumer.close();
                latch.countDown(); // 通知main code自己已经完成,计数减一
            }
        }

        public void shutdown(){ //停止consumer thread
            // wakeup这个特别的方法去interrupt consumer.poll()
            // it will throw the exception WakeUpException
            consumer.wakeup();
        }
    }


}
