package com.tanknavy.kafka.basic;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;


public class ProducerDemoKeys {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);

        String bootstrapServers = "127.0.0.1:9092";

        // create kafka producer properties
        Properties properties = new Properties();
        //properties.setProperty("bootstrap.servers","localhost:9092");
        // 使用这用方法设置kafka参数名称
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer",StringSerializer.class.getName());
        // 安全producer参数
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true"); //幂等
        properties.setProperty(ProducerConfig.ACKS_CONFIG,"all"); // in-sync复制
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE)); //最大重试
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");//未应答request在一个连接中最大发送个数
        //消息压缩，批量消息，gzip,Lz3,snappy
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy"); //消息压缩格式
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG,"20"); //默认为0，消息等待多久再发送
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG,Integer.toString(32 * 1024));

        // create the producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        for(int i=0;i<10;i++) {
            // create a producer record
            String topic = "first_topic";
            String value = "ProducerDemoWithCallback" + Integer.toString(i);
            // 除了topic和要发送的值，还可以指定key，这样在发给partition时除了round robin还可以hash
            String key = "id_" + Integer.toString(i);

            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>(topic, key, value);

            // 相同的key,kafka保证发送到同一个partition
            logger.info("Key: " + key); // log the key
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
            }).get(); // block the .send() to make it synchronous --生产环境不要这样做
        }
        producer.flush();
        producer.close();
    }
}

