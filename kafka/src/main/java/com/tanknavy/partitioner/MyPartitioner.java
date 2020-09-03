package com.tanknavy.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * Author: Alex Cheng 7/14/2020 1:13 PM
 * props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.tanknavy.partitioner.MyPartitioner");//全类名通过反射的方式构建对象
 *
 */
public class MyPartitioner implements Partitioner {//已经序列化了
    @Override
    public int partition(String topic, Object key, byte[] bytes, Object value, byte[] bytes1, Cluster cluster) {
        //根据业务逻辑判断分区
        //Integer size = cluster.partitionCountForTopic(topic); //几个分区
        List<PartitionInfo> partitionInfos = cluster.availablePartitionsForTopic(topic);
        int size = partitionInfos.size();
        //return key.toString().hashCode() % size;//key的hash值取模

        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
