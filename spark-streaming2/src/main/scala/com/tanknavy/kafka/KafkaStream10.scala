package com.tanknavy.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.TopicPartition
import org.apache.spark.{SparkConf, TaskContext}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, HasOffsetRanges, KafkaUtils, LocationStrategies, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * Author: Alex Cheng 7/7/2020 9:06 PM
 */

//0.8版本，手动读取保存offset
object KafkaStream10 {

  //0.8版本使用KafkaCluster手工管理offset， TopicAndPartition对应的offset值
 /* def getOffset(kafkaCluster: KafkaCluster, group: String, topic: String) = {

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

        if(ack.isLeft){ //保存失败
          println(s"Error:${ack.left.get}")
        }else{ //保存成功
          println(s"Success:${offset}")
        }
      }

    })

  }
*/

  //-----------------------------------------------------
  def main(args: Array[String]): Unit = {

    //创建SparkConf
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("kafkaStreaming")

    //创建StreamingContext
    val ssc = new StreamingContext(sparkConf,Seconds(5))

    //创建Kafka参数,broker，topic, consumeGroup, deserializable,offset
    val brokers = "spark1:9092,spark3:9092"
    val topic = "stream"
    val group = "bigdata"
    val deserialization = "org.apache.kafka.common.serialization.StringDeserializer"


    //val kafkaParams = Map[String, Object](
    val kafkaParams = Map[String, String](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> group,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> deserialization,
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> deserialization
      //ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest", //"earliest"
      //ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (false:java.lang.Boolean)
    )
    val topics = Array("topicA", "topicB")

    //两个工具类，KafkaUtils, KafkaCluster保存offset
    //怎么维护offset?需要保存offset，读取offset, 工具类KafkaCluster, 0.8版本才有，0.10以上
    //https://spark.apache.org/docs/latest/streaming-kafka-0-10-integration.html //offset
    //https://jaceklaskowski.gitbooks.io/spark-streaming/spark-streaming-kafka-KafkaUtils.html
    //https://jaceklaskowski.gitbooks.io/spark-streaming/spark-streaming-kafka-KafkaRDD.html
    //https://www.codenong.com/js07dac5821265/ //保存offset到zookeeper
    //https://blog.cloudera.com/offset-management-for-apache-kafka-with-apache-spark-streaming/ //保存offset到Hbase, zookeeper, kafka itselt
    //https://www.learningjournal.guru/courses/kafka/kafka-foundation-training/exactly-once-processing/ 保存offset到mysql

    //val kafkaCluster = new KafkaCluster(kafkaParams)
    //val fromOffset: Map[TopicAndPartition, Long] = getOffset(kafkaCluster,group, topic)

    //0.8读取Kafka数据创建DStream
 /*   val kafkaStream: InputDStream[String] = KafkaUtils.createDirectStream(ssc,
      kafkaParams,
      fromOffset,
      (message: MessageAndMetadata[String, String]) => message.message() //输入的流数据是String类型，只拿value
    )*/


    //1.0新版本，怎么读取offset?
    //启动时读取最新offset
    val fromOffsets:Map[TopicPartition,Long] = selectOffsetsFromYourDatabase.map { resultSet =>
      new TopicPartition(resultSet.string("topic"), resultSet.int("partition")) -> resultSet.long("offset")
    }.toMap

    //无offset启动stream
    val kafkaStream = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams)
    )

    //读取offset启动stream
    val kafkaStreamffset = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      //ConsumerStrategies.Subscribe[String, String](topics, kafkaParams),
      ConsumerStrategies.Assign[String, String](fromOffsets.keys.toList, kafkaParams, fromOffsets)
    )


    //简单的业务处理
    //kafkaStream.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    //offset的保存，高阶api是异步保存offset有可能丢失，这里采用手动保存offset
    //最新offset, group,保存路径
    //setOffset(kafkaCluster, kafkaStream, group )
    kafkaStream.foreachRDD { rdd =>
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      //查看当前rdd的offset
      rdd.foreachPartition { iter =>
        val o: OffsetRange = offsetRanges(TaskContext.get.partitionId) //shuffle或者repartition后rdd和kafka的partition不会一一对应
        println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}") //打印offset
      }

      // some time later, after outputs have completed
      //kafkaStream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)//可选择让kafka异步提交

      val results = yourCalculation(rdd)

      // begin your transaction

      // update results
      // update offsets where the end of existing offsets matches the beginning of this batch of offsets
      // assert that offsets were updated correctly

      // end your transaction
    }


    //启动

  }
}
