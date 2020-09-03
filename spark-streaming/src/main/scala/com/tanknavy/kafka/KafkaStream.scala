package com.tanknavy.kafka

import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaCluster, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.kafka.KafkaCluster.Err
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

/**
 * Author: Alex Cheng 7/7/2020 9:06 PM
 * java包冲突分析https://www.programmersought.com/article/357213254/
 *
 */

//0.8版本，手动读取保存offset
object KafkaStream {

  //0.8版本使用KafkaCluster手工管理offset， TopicAndPartition对应的offset值
  def getOffset(kafkaCluster: KafkaCluster, group: String, topic: String) = {

    //存放topic和partition的offset值
    val topicAndPartitionToLong = new mutable.HashMap[TopicAndPartition,Long]()//返回值

    //获取分区信息,Either类似Option，可能有，也可能没有
    val partitions: Either[Err, Set[TopicAndPartition]] = kafkaCluster.getPartitions(Set(topic))

    if(partitions.isRight){ //是否有分区信息
      //取出分区信息
      val topicAndPartitions: Set[TopicAndPartition] = partitions.right.get

      //获取消费者的offset
      val topicAndPartitionsToOffset: Either[Err, Map[TopicAndPartition, Long]] = kafkaCluster.getConsumerOffsets(group,topicAndPartitions)

      //没有消费国
      if(topicAndPartitionsToOffset.isLeft){ //没有正确的值，就是没有消费过，设置为0
        //topic和partition的offset初始化为0
        for(topicAndPartion <- topicAndPartitions){
          topicAndPartitionToLong +=(topicAndPartion -> 0L) //放到map
        }
      }else{//有值,已经消费过的topic/partition,从保存过的地方取值
        val offsets: Map[TopicAndPartition, Long] = topicAndPartitionsToOffset.right.get
        for(offset <- offsets){
          topicAndPartitionToLong += offset
        }
        //topicAndPartitionToLong += offsets
      }
    }
    topicAndPartitionToLong.toMap //返回offset

  }

  //手动保存offset
  def setOffset(kafkaCluster: KafkaCluster, kafkaStream: InputDStream[String], group: String): Unit = {

    kafkaStream.foreachRDD(rdd =>{ //每个rdd都有offset range
      //从rdd中取出offset
      val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

      //遍历offset ranges
      for(offsetRange <- offsetRanges){
        val offset: Long = offsetRange.untilOffset

        //保存offset
        val ack: Either[Err, Map[TopicAndPartition, Short]] = kafkaCluster.setConsumerOffsets(group, Map(offsetRange.topicAndPartition() ->offset))

        //检查保存结果
        if(ack.isLeft){ //保存失败
          println(s"Error to save offset: ${ack.left.get}")
        }else{ //保存成功
          println(s"Success to save offset: ${offset}")
        }
      }

    })

  }



  def main(args: Array[String]): Unit = {

    //创建SparkConf
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("kafkaStreaming")
    //val sparkConf: SparkConf = new SparkConf().setMaster("spark://spark3:7077").setAppName("kafkaStreaming")

    //创建StreamingContext
    val ssc = new StreamingContext(sparkConf,Seconds(5))

    //创建Kafka参数,broker，topic, consumeGroup, deserializable,offset
    val brokers = "spark1:9092,spark3:9092"
    val topic = "stream"
    val group = "bigdata"
    val deserialization = "org.apache.kafka.common.serialization.StringDeserializer"
    //val topics = Array("topicA", "topicB")

    //val kafkaParams = Map[String, Object](
    val kafkaParams = Map[String, String](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> group,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> deserialization,
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> deserialization
      //ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest", //"earliest"
      //ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (false:java.lang.Boolean)
    )


    //两个工具类，KafkaUtils, KafkaCluster保存offset
    //怎么维护offset?需要保存offset，读取offset, 工具类KafkaCluster, 0.8版本才有，0.10以上
    //https://spark.apache.org/docs/latest/streaming-kafka-0-10-integration.html //offset
    //https://jaceklaskowski.gitbooks.io/spark-streaming/spark-streaming-kafka-KafkaUtils.html
    //https://jaceklaskowski.gitbooks.io/spark-streaming/spark-streaming-kafka-KafkaRDD.html
    //https://www.codenong.com/js07dac5821265/ //保存offset到zookeeper
    //https://blog.cloudera.com/offset-management-for-apache-kafka-with-apache-spark-streaming/ //保存offset到Hbase, zookeeper, kafka itselt
    //https://www.learningjournal.guru/courses/kafka/kafka-foundation-training/exactly-once-processing/ 保存offset到mysql

    val kafkaCluster = new KafkaCluster(kafkaParams)
    val fromOffset: Map[TopicAndPartition, Long] = getOffset(kafkaCluster,group, topic)

    //读取Kafka数据创建DStream
    val kafkaStream: InputDStream[String] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, String](ssc,
      kafkaParams,
      fromOffset, //从那个offset开始, 重启sparkstreaming后还能从上次的offset接着消费
      (message: MessageAndMetadata[String, String]) => message.message() //输入的流数据是String类型，只拿value
    )

    //简单的业务处理
    kafkaStream.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    //offset的保存，高阶api异步(现在有同步的)保存offset有可能丢失这里采用手动保存offset
    //最新offset, group,保存路径
    setOffset(kafkaCluster, kafkaStream, group )

    //开启任务
    ssc.start()
    ssc.awaitTermination()


  }
}
