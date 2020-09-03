package com.tanknavy.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import scala.collection.mutable.HashMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Author: Alex Cheng 7/14/2020 5:39 PM
 */
public class ConsumerOffset2Location {
    public static void main(String[] args) {

        //对消费者而言，想实现exactly once
        //保存offset到自定义的位置，mysql,hbase,zookeeper, 都可以，这里是内存中的map， 后续保存到mysql
        //ConsumerRebalanceListener
        HashMap<TopicPartition, Long> currentOffset = new HashMap<>();

        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "spark3:9092,spark1:9091");

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//开启自动提交offset,可以手动提交,
        //手动提交offset的方法有两种，commitSync和commitAsync，都会将本次poll的批量数据中最高偏移量提交，
        // 一个阻塞当前线程，一直到成功，并且会自动重试，一个不会阻塞，也没有失败重试机制，所以有可能提交失败
        //props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");//自动提交offset的延时，一秒提交一次
        //ctrl+n 查一下
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        //消费者组,在console中有默认的随机分配，
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bigdata");
        //如果想从最初offset开始消费，earliest, latest两个可选，默认latest, 注意这时需要更改group！满足两个条件
        //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");//当没有初始化的offset时(比如消费者组第一次)，或者当前offset已经不存在，否则不能reset

        //创建消费者
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(props);

        //使用自定义offset存储路径，
        consumer.subscribe(Collections.singletonList("temp"), new ConsumerRebalanceListener() {
            //Rebalance之前调用
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                commitOffset(currentOffset);//正常提交
            }

            //Rebalance之后调用
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                currentOffset.clear(); //map置空
                for (TopicPartition partition : partitions) {
                    consumer.seek(partition, getOffset(partition));//seek定位到最近提交的offset位置继续消费
                }

            }

        });//订阅集合，Arrays.asList()



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

            //手动提交，先关闭自动提交
            //提交时机的把握，太快可能丢失数据，太慢可能重复消费数据，可以自定义存储offset，维护offset相当繁琐，
            // 需要考虑到消费者组变化后的rebalance，借助ConsumerRebalanceListener
            //之前是在zookeeper, 现在默认是kafka本地，假如业务处理需要保存到mysql, 可以和提交offset到mysql一起组成一个transaction
            //同步提交，线程阻塞直到成功
            //consumer.commitSync();
            //异步提交，

//            consumer.commitAsync(new OffsetCommitCallback() {
//                @Override
//                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsetsMap, Exception e) {
//                    if(e != null){
//                        System.out.println("提交失败" + offsetsMap);
//                    }
//                }
//            });//callback可以处理异常

            commitOffset(currentOffset);//异步提交

        }

        //关闭连接
        //consumer.close();

    }

    //自己维护offset储存的话
    //获取某个分区的最新offset
    //https://spark.apache.org/docs/latest/streaming-kafka-0-10-integration.html //offset
    //https://jaceklaskowski.gitbooks.io/spark-streaming/spark-streaming-kafka-KafkaUtils.html
    //https://jaceklaskowski.gitbooks.io/spark-streaming/spark-streaming-kafka-KafkaRDD.html
    //https://www.codenong.com/js07dac5821265/ //保存offset到zookeeper
    //https://blog.cloudera.com/offset-management-for-apache-kafka-with-apache-spark-streaming/ //保存offset到Hbase, zookeeper, kafka itself
    //https://www.learningjournal.guru/courses/kafka/kafka-foundation-training/exactly-once-processing/ 保存offset到mysql,做一个事物
    private static long getOffset(TopicPartition partition) {
        return 0;
    }

    //提交该消费者所有分区的offset, 假设保存到mysql，使用jdbc,建表
    private static void commitOffset(HashMap<TopicPartition, Long> currentOffset) {
    }

}
