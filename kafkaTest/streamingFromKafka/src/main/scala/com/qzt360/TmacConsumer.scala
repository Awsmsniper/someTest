package com.qzt360

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils

/**
  * Created by zhaogj on 19/12/2016.
  * spark-submit --master yarn-cluster --class com.qzt360.TmacConsumer streamingFromKafkaTest-1.0-SNAPSHOT.jar
  */
object TmacConsumer {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("NetworkWordCount")
    val ssc = new StreamingContext(conf, Seconds(10))
    val zkQuorum = "192.168.10.12:2181"
    val group = "com.qzt360"
    val topics = "tmac"
    val numThreads = 1

    ssc.checkpoint("checkpoint")

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val stream = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap)
    stream.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
