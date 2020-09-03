import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * Author: Alex Cheng 7/15/2020 1:07 PM
 * <p>
 * ElasticSearch 7.6.2 client
 */

//https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html
public class EsApp {
    TransportClient client;

    //获取客户端对象
    //@Test//测试成功
    @Before //Test测试成功后给每个方法前调用
    public void getClient() throws UnknownHostException {

        //获取连接的es
        Settings settings = Settings.builder().put("cluster.name", "es-spark").build();
        //获取客户端对象
        //PreBuiltTransportClient client = new PreBuiltTransportClient(settings);
        client = new PreBuiltTransportClient(settings);//返回父类对象,
        //可以添加多个地址
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("spark1"), 9300));//web操作9200，api操作9300

        System.out.println(client.toString());
    }

    //创建索引,创建索引的几种方式
    @Test
    public void createIndex() {
        client.admin().indices().prepareCreate("blog3").get();//和类似DDL相关习需要client取得admin对象

        client.close();
    }

    //删除索引
    @Test
    public void deleteIndex() {
        client.admin().indices().prepareDelete("blog").get();
        client.close();
    }

    //新建文档(相当于向DB中插入数据)
    //源数据json串
    @Test
    public void createIndexByJson() throws IOException {
        //文档内容
        String json = "{" +
                "\"id\":\"kimchy\"," +
                "\"title\":\"2013-01-30\"," +
                "\"content\":\"trying out Elasticsearch\"" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        String stringifiedJson = null;

        stringifiedJson = objectMapper.writeValueAsString(json);

        //插入一个document
        IndexResponse indexResponse = client.prepareIndex("blog", "article", "1").setSource(json).execute().actionGet();

        System.out.println("索引" + indexResponse.getIndex()
                + "id" + indexResponse.getId()
                + "版本号" + indexResponse.getVersion()
                + "分片" + indexResponse.getShardInfo()
                + "结果" + indexResponse.getResult());

        client.close();

    }


    //源数据map方式添加json
    //新建文档(相当于向DB中插入数据)
    //源数据json串
    @Test
    public void createIndexByMap() throws IOException {
        //文档内容
        String json = "{\" + \"\\\"id\\\":\\\"1\\\",\" + \"\\\"title\\\":\\\"基于Lucene的搜索服务器\\\",\"\n" +
                "\t\t\t\t+ \"\\\"content\\\":\\\"它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口\\\"\" + \"}\n";


        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", "2");
        jsonMap.put("title", "moneyComeIn");
        jsonMap.put("content", "good boy, you can do it!");


        //插入一个document
        //IndexResponse indexResponse = client.prepareIndex("blog", "article", "1").setSource(json).execute().actionGet();
        IndexResponse indexResponse = client.prepareIndex("blog", "article", "2").setSource(jsonMap).execute().actionGet();
        System.out.println("索引" + indexResponse.getIndex()
                + "id" + indexResponse.getId()
                + "版本号" + indexResponse.getVersion()
                + "分片" + indexResponse.getShardInfo()
                + "结果" + indexResponse.getResult());

        client.close();

    }

    //源数据es构建起添加json
    @Test
    public void createIndexByBuilder() throws IOException {
        //文档内容
        String json = "{\" + \"\\\"id\\\":\\\"1\\\",\" + \"\\\"title\\\":\\\"基于Lucene的搜索服务器\\\",\"\n" +
                "\t\t\t\t+ \"\\\"content\\\":\\\"它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口\\\"\" + \"}\n";

        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("id", "3")
                .field("title", "dollar")
                .field("content", "millions dollar for me")
                .endObject();


        //插入一个document
        //IndexResponse indexResponse = client.prepareIndex("blog", "article", "1").setSource(json).execute().actionGet();
        //IndexResponse indexResponse = client.prepareIndex("blog", "article", "2").setSource(jsonMap).execute().actionGet();
        IndexResponse indexResponse = client.prepareIndex("blog", "article", "3").setSource(builder).execute().actionGet();


        System.out.println("索引" + indexResponse.getIndex()
                + "id" + indexResponse.getId()
                + "版本号" + indexResponse.getVersion()
                + "分片" + indexResponse.getShardInfo()
                + "结果" + indexResponse.getResult());

        client.close();

    }


    //搜索文档数据(单个索引)
    @Test
    public void queryIndex() {
        GetResponse response = client.prepareGet("blog", "article", "2").get();

        System.out.println(response.getSourceAsString());

        client.close();
    }

    @Test
    public void queryMultiData() {
        MultiGetResponse multiDatas = client.prepareMultiGet().add("blog", "article", "2").add("blog", "article", "3", "2").get();

        for (MultiGetItemResponse multiData : multiDatas) {
            //最好判断是否存在
            GetResponse response = multiData.getResponse();
            if (response.isExists()) {
                System.out.println(response.getSourceAsString());
            }
        }

        Iterator<MultiGetItemResponse> iterator = multiDatas.iterator();
        while (iterator.hasNext()) {
            MultiGetItemResponse next = iterator.next();
            System.out.println(next.getResponse().getSourceAsString());
        }
    }


    //文档的更新和删除
    @Test
    public void updateIndex() throws IOException, ExecutionException, InterruptedException {
        //UpdateRequest updateRequest = new UpdateRequest();
        UpdateRequest updateRequest = new UpdateRequest("blog", "article", "2");

        updateRequest.doc(XContentFactory.jsonBuilder()
                .startObject().field("id", "2")
                .field("article", "I'm rich")
                .field("content", "you deserved millions dollar")
                .endObject());
        UpdateResponse updateResponse = client.update(updateRequest).get();
        System.out.println("索引" + updateResponse.getIndex()
                + "id" + updateResponse.getId()
                + "版本号" + updateResponse.getVersion()
                + "分片" + updateResponse.getShardInfo()
                + "结果" + updateResponse.getResult());


        //upsert------------查询条件
        IndexRequest indexRequest = new IndexRequest("blog", "article", "5").source(XContentFactory.jsonBuilder().startObject()
                .field("title", "引擎").field("content", "future and fortune").field("id", "5").endObject());

        //upsert, 更新/插入,有则更新，无则插入
        UpdateRequest upsert = new UpdateRequest("blog", "article", "5").doc(XContentFactory.jsonBuilder()
                .startObject().field("id", "5")
                .field("article", "I'm rich")
                .field("content", "you deserved millions dollar")
                .endObject()).upsert(indexRequest);
        //upsert
        UpdateResponse upsertResponse = client.update(upsert).get();


        System.out.println("索引" + updateResponse.getIndex()
                + "id" + updateResponse.getId()
                + "版本号" + updateResponse.getVersion()
                + "分片" + updateResponse.getShardInfo()
                + "结果" + updateResponse.getResult());

        client.close();

    }


    //文档删除
    @Test
    public void deleteDocument() {
        client.prepareDelete("blog", "article", "5").get();

        client.close();

    }

    //-------------------------------------------------------------
    //条件查询QueryBuilder,查询具体文档，二十多个查询方式
    //查询所有(matchAllQuery)
    @Test
    public void queryMatchAll() {
        //SearchResponse response = client.prepareSearch("blog").setSearchType("article").setQuery(QueryBuilders.matchAllQuery()).get();
        SearchResponse response = client.prepareSearch("blog").setTypes("article").setQuery(QueryBuilders.matchAllQuery()).get();

        SearchHits hits = response.getHits();

        System.out.println("查询结果：" + hits.getTotalHits());

        Iterator<SearchHit> iterator = hits.iterator();

        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            System.out.println(next.getSourceAsString());
        }
        client.close();

    }


    //所有字段分词查询(queryStringQuery), 比如搜索“全文”将会是"全","文”以及"全文"
    @Test
    public void queryStringSplit() {

        SearchResponse response = client.prepareSearch("blog").setTypes("article")
                .setQuery(QueryBuilders.queryStringQuery("dollar billion millions")).get();//分词查询

        SearchHits hits = response.getHits();

        System.out.println("查询结果：" + hits.getTotalHits());

        Iterator<SearchHit> iterator = hits.iterator();

        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            System.out.println(next.getSourceAsString());
        }
        client.close();

    }


    //词条查询(TermQuery)
    @Test
    public void termQuery() {

        SearchResponse response = client.prepareSearch("blog").setTypes("article")
                .setQuery(QueryBuilders.termQuery("content", "millions dollar")).get();//词条查询时默认是每个词，所以这个两个词的不存在

        SearchHits hits = response.getHits();

        System.out.println("查询结果：" + hits.getTotalHits());

        Iterator<SearchHit> iterator = hits.iterator();

        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            System.out.println(next.getSourceAsString());
        }
        client.close();

    }


    //通配符查询(wildcardQuery),*表示多个字符，？表示单个字符
    @Test
    public void wildcardQuery() {

        SearchResponse response = client.prepareSearch("blog").setTypes("article")
                .setQuery(QueryBuilders.wildcardQuery("content", "*do*")).get();//词条查询时默认是每个词，所以这个两个词的不存在

        SearchHits hits = response.getHits();

        System.out.println("查询结果：" + hits.getTotalHits());

        Iterator<SearchHit> iterator = hits.iterator();

        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            System.out.println(next.getSourceAsString());
        }
        client.close();

    }


    //模糊查询(fuzzy),使用最多的
    @Test
    public void fuzzyQuery() {

        SearchResponse response = client.prepareSearch("blog").setTypes("article")
                .setQuery(QueryBuilders.fuzzyQuery("content", "DollarS")).get();//词条查询时默认是每个词，所以这个两个词的不存在

        SearchHits hits = response.getHits();

        System.out.println("查询结果：" + hits.getTotalHits());

        Iterator<SearchHit> iterator = hits.iterator();

        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            System.out.println(next.getSourceAsString());
        }
        client.close();

    }

    //映射:相当于多索引创建约束条件
    //必须有index，没有mapping时，然后导入document数据之前
    @Test
    public void Mapping() throws IOException, ExecutionException, InterruptedException {
        // 1设置mapping
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                    .startObject("article")
                        .startObject("properties")
                            .startObject("id1")
                                .field("type", "text")
                                .field("store", "true")
                            .endObject()
                            .startObject("title2")
                                .field("type", "text")
                                .field("store", "false")
                            .endObject()
                            .startObject("content")
                                .field("type", "text")
                                .field("store", "true")
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject();

        // 2 添加mapping
        PutMappingRequest mapping = Requests.putMappingRequest("blog2").type("article").source(builder);

        client.admin().indices().putMapping(mapping).get();


        // 3.关闭资源
        client.close();
    }


    //过滤

    //聚合


}
