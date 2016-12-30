package org.after90.spark

/**
  * Created by zhaogj on 30/12/2016.
  */
import org.apache.spark.{SparkConf, SparkContext}
object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val textRDD = sc.textFile("/Users/zhaogj/tmp/spark/sparkinput.txt")
    println(textRDD.count())
  }
}
