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

//按住ctrl显示方法签名
public class ConsumerDemoWithThread {
    public static void main(String[] args) {
        new ConsumerDemoWithThread().run();
        //create consumer
        //KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        // subscribe consumer to topic
        //consumer.subscribe(Collections.singletonList(topic));
        //consumer.subscribe(Arrays.asList("first_topic","second_topic"));
        // poll for new data
    }

    private ConsumerDemoWithThread(){

    }

    private void run(){
        Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThread.class.getName());

        String bootstrapServers = "127.0.0.1:9092";
        String topic = "first_topic";
        String groupId = "my-fifth-application";
        CountDownLatch latch = new CountDownLatch(1);

        //一种方法是ConsumerThread变成静态
        // 第二种方法呢?思路：实例化自己，将main方法中的代码已到自己类的run方法中
        Runnable myConsumer = new ConsumerThread(latch,bootstrapServers,groupId,topic);
        Thread thread = new Thread(myConsumer);
        thread.start();

        // add shutdownhook
        //The goal was to write code to call start() and join() in one place. Parameter anonymous class is an anonymous function. new Thread(() ->{})
        //主线程正常退出或者被中断时，调用停机钩子程序去停止子程序
        Runtime.getRuntime().addShutdownHook(new Thread( () ->{ // lambda表达式
            logger.info("Caught shutdown hook");
            ((ConsumerThread) myConsumer).shutdown();
            try {
               latch.await();
            } catch (InterruptedException e) { //一定要在exit时才能看到效果
                e.printStackTrace();
            }
           logger.info("Application has exited");
        } ) );

        //正常结束
        try {
            latch.await(); //当前线程等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            logger.info("Application is closing");
        }

    }

    //public static class ConsumerThread implements Runnable{ //一种方法是ConsumerThread变成静态
    public class ConsumerThread implements Runnable{

        private CountDownLatch latch;
        private KafkaConsumer<String,String> consumer;
        private Logger logger = LoggerFactory.getLogger(ConsumerThread.class.getName());
        private String topic;

        public ConsumerThread(CountDownLatch latch, String bootstrapServers, String groupId, String topic ){
            this.latch = latch;
            this.topic = topic;
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
               latch.countDown(); // 通知main code自己已经完成
            }
        }

        public void shutdown(){
            // wakeup这个特别的方法去interrupt consumer.poll()
            // it will throw the exception WakeUpException
            consumer.wakeup();
        }
    }

}
