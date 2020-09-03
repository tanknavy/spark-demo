package com.tanknavy.mr02;

import com.tanknavy.mr01.FruitDriver;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;//<!--hbase 1.3.1和2.0.6相比，后者还需要hbase-mapreduce-->
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Author: Alex Cheng 7/11/2020 10:32 AM
 */
public class Fruit2Driver implements Tool {

    private Configuration configuration = null;

    @Override
    public int run(String[] args) throws Exception {

        //0.检查输入参数
        /*if (args.length != 2) {
            System.out.println("Please provid two arguments :");
            System.out.println("[ 1 ] Input table name");
            System.out.println("[ 2 ] Output table name");
            return -1;
        }
        System.out.println("args[0]:" + args[0]);
        System.out.println("args[1]:" + args[1]);*/
        if(configuration!=null){
            System.out.println("-------配置信息cq: " + configuration.get("cq"));
        }

        //1.获取job对象
        Job job = Job.getInstance(configuration, this.getClass().getName());

        //2.设置主类路径
        job.setJarByClass(Fruit2Driver.class);

        //3.设置mapper&输出k/v类型
        TableMapReduceUtil.initTableMapperJob("fruit", //本地测试
        //TableMapReduceUtil.initTableMapperJob(args[0],
                new Scan(),
                Fruit2Mapper.class,
                ImmutableBytesWritable.class,
                Put.class,
                job);

        //4.设置reducer&输出k/v类型
        TableMapReduceUtil.initTableReducerJob("fruit3", Fruit2Reducer.class, job);//本地测试
        //TableMapReduceUtil.initTableReducerJob(args[1], Fruit2Reducer.class, job);

        //5.输入输出参数
        //上面两个方法已经设定了输入输出table参数了

        //6.提交任务
        boolean result = job.waitForCompletion(true);

        return result?0:1;
    }

    @Override
    public void setConf(Configuration conf) {
        configuration = conf;
    }

    @Override
    public Configuration getConf() {
        return configuration;
    }


    public static void main(String[] args) {

        try {
            //Configuration conf = new Configuration();//只能打包放到mr集群上跑
            //如果想在本地运行方法一，使用Hbase集群的配置
            Configuration configuration = HBaseConfiguration.create();//默认读取了hbase-default.xml和hbase-site.xml文件
            //configuration.set("hbase.zookeeper.quorum", "spark1,spark3"); //找hbase-site.xml中配置,本地没有可以手工指定
            //如果想在本地运行方法二，赋值hbase-site.xml文件到resources下

            configuration.set("cq", "name");//在mpa/reduce中参数

            int run = ToolRunner.run(configuration, new Fruit2Driver(), args);
            System.exit(run);//返回run执行结果

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
