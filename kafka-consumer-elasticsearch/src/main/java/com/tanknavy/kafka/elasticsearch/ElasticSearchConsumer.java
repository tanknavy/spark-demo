package com.tanknavy.kafka.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;


/**
 * https://app.bonsai.io/clusters/kafka-testing-9798227900/credentials
 */
public class ElasticSearchConsumer {

    public static void main(String[] args) throws IOException {
        Logger logger = LoggerFactory.getLogger(ElasticSearchConsumer.class.getName());

        //connect elastic search
        RestHighLevelClient client = createClient();

        // test elastic server
        //String jsonString = "{\"foot\": \"bar\" }";
        //use the typeless endpoints instead (/{index}/_doc/{id}, /{index}/_doc, or /{index}/_create/{id}).
        //IndexRequest indexRequest = new IndexRequest("twitter", "tweets").source(jsonString, XContentType.JSON);
        //IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        //String id = indexResponse.getId(); //
        //logger.info("ElasticSearch id: " + id);

        // kafka consumer
        KafkaConsumer<String,String> consumer = createConsumer("twitter_tweets");

        // poll for new data
        while(true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String,String> record: records){
                //logger.info("key: " + record.key() +",value: " + record.value());
                //logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
                // where insert data into ElasticSearch
                IndexRequest indexRequest = new IndexRequest(
                        "twitter" ,
                        "tweets" // 过期，使用typeless方法
                ).source(record.value(), XContentType.JSON); // 推文内容

                IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

                String id = indexResponse.getId();
                logger.info(id);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // close es connection
        //client.close();

    }

    public static RestHighLevelClient createClient() {

        //https://hhrzyftp43:z6902s4r3h@kafka-testing-9798227900.us-west-2.bonsaisearch.net:443
        String hostname = "kafka-testing-9798227900.us-west-2.bonsaisearch.net";
        String username = "hhrzyftp43";
        String password = "z6902s4r3h";

        //don't do if run a local ES;
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));


        RestClientBuilder builder = RestClient.builder(
                new HttpHost(hostname, 443,"https")).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        });
        //同上，改为lambda表达
        /*
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(hostname, 443,"https")).setHttpClientConfigCallback(httpAsyncClientBuilder ->
                    httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
         */

        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }


    public static KafkaConsumer<String,String> createConsumer(String topic){

        String bootstrapServers = "127.0.0.1:9092";
        //String topic = "twitter_tweets";
        String groupId = "kafka-elasticsearch";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest"); //earliest/latest/none

        //create consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(topic));

        return consumer;

    }

}
