package com.tanknavy.serder

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{DataFrame, Dataset, KeyValueGroupedDataset, SparkSession}

/**
 * Author: Alex Cheng 7/2/2020 10:50 PM
 */

object KyroSerder {
  def main(args: Array[String]): Unit = {
    //创建SparkConf
//    val conf = new SparkConf().setAppName("WordCount")
//    //val conf2 = new SparkConf().setAppName("WordCount").setMaster("local[*]") //如果想local模式测试一下，不放到集群/yarn上面
//
//    //创建SparkContext对象
//    val sc = new SparkContext(conf) // 对象ctrl+alt+v(scala), alt+enter(java)， ctrl+p, alt+\ 显示参数列表

    //Spark Session创建
    val spark: SparkSession = SparkSession
      .builder() //在伴生对象中
      .master("local[*]")
      .appName("sparkConversion")
      //.enableHiveSupport()
      .config("spark.sql.shuffle.partitions",2) //spark sql并行度
      .getOrCreate()

    //读取文件
//    val lines: RDD[String] = sc.textFile(args(0))
//    val words: RDD[String] = lines.flatMap(_.split(" "))
//    val wordAndCount: RDD[(String, Int)] = words.map((_,1)).reduceByKey(_+_)

    //输出保存到文件中
    //wordAndCount.saveAsTextFile(args(1))

    //groupByKey没有预聚合，使用aggregate代替
    //https://medium.com/build-and-learn/spark-aggregating-your-data-the-fast-way-e37b53314fad
    val rdd = spark.sparkContext.parallelize(Seq(("aa",1,"a"),("bb",2,"b"),("cc",3,"x"),("dd",4,"d"),("aa",2,"a"),("bb",0,"y"),("cc",4,"c"),("dd",5,"d"),("aa",9,"a"),("cc",7,"c")))
    import spark.implicits._ //toDF,toDS需要隐式类
    val columns = Seq("fname", "num", "lname")
    val df: DataFrame = rdd.toDF(columns:_*)
    //val ds: Dataset[Person] = rdd.toDS().as[Person] //rdd要先map才能as[T]
    val ds: Dataset[Person] = rdd.map(row =>Person(row._1,row._2, row._3)).toDS().as[Person] //rdd要先map才能as[T]

    val groupDs: KeyValueGroupedDataset[String, Person] = ds.groupByKey(_.fname) //rdd和ds的groupByKey参数含义不一样
    val reduceRs: Dataset[(String, Person)] = groupDs.reduceGroups((p1,p2) => personMaxTwo(p1,p2)) //同一个人名下最大num

    reduceRs.foreach(p => println(p._2.fname))

    //上述groupByKey改为aggregateByKey,有预聚合功能
    val rddForTuple2 = spark.sparkContext.parallelize(Seq(("aa",1),("bb",2),("cc",3),("dd",4),("aa",2),("bb",3),("cc",4),("dd",5),("aa",6),("cc",7)))
    //只有Tuple2形式的rdd才可以直接aggregateByKey, Tuple3以上使用groupBy, 但是ds的groupByKey对tuple3以上都可以
    val aggRddT2: RDD[(String, Int)] = rddForTuple2.aggregateByKey(0)(_+_, _+_) //初始值，分区内合并，分区间合并
    val groupRddT3: RDD[(String, Iterable[(String, Int, String)])] = rdd.groupBy(_._1) //使用Tuple3中第一个元素作为key

    //tuple2的格式RDD才能直接ByKey, 默认第一个键，除非先groupBy()，对多个元素可用groupBy(col1,col2)
    //对tuple3格式，使用aggregateByKey
    val person:Person = Person("",0,null) //小心null比较
    val aggRddT3 = ds.rdd.map(p =>(p.fname, p)).aggregateByKey(Person("",0,""))((p1,p2) => personMax(Seq(p1,p2)), (p3,p4) =>personMax(Seq(p3,p4)))
      .map(_._2).toDS().as[Person] //又转回ds
    val aggRddT3b = ds.rdd.map(p =>(p.fname, p)).aggregateByKey(person)(personMaxTwo, personMaxTwo)
      .map(_._2).toDS().as[Person]

    println("person compare, first compare lastName, then the num")
    aggRddT2.foreach(println)
    aggRddT3.foreach(p =>println(p))

    //添加随机key的UDF,使用随机分区键
    import org.apache.spark.sql.functions._ //spark.sql下的udf函数产生UDF
    val partition_per_hour = 16
    val appendRandomKey: UserDefinedFunction = udf((hourId: String) => hourId + (Math.random * partition_per_hour).toInt.toString)
    val appendRandomKey2 = udf{hourId: String => hourId + (Math.random * partition_per_hour).toInt.toString}
    val df_new: DataFrame = df.withColumn("partition_key", appendRandomKey(col("num"))) //添加分区栏位
        .repartition(col("partition_key")) //使用分区栏位分区
        .drop(col("partition_key")) //用完删除分区栏位
    println("====use random partition key===")
    df_new.foreach(row =>println(row))

    //关闭连接
    //sc.stop()
    spark.stop()
  }

  def personMax(input: Seq[Person]) = { //列表中返回最大
    input.maxBy(p => (p.lname, p.num)) //比较
  }

  def personMaxTwo(p1:Person, p2:Person) = { //列表中返回最大
    Seq(p1,p2).maxBy(p => (p.lname, p.num)) //比较
  }

  case class Person(fname:String, num: Int, lname: String)

  //DataSet中对栏位定制Aggregator(UDAF)
  //https://spark.apache.org/docs/2.4.0/api/scala/index.html#org.apache.spark.sql.expressions.Aggregator
  object AggMaxPerson{

  }
}
