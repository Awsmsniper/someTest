package org.after90.test

/**
  * Created by zhaogj on 10/18/16.
  */

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val textRDD = sc.textFile("/Users/zhaogj/tmp/spark/sparkinput.txt")
    println(textRDD.count())
    //val tmpRdd = textRDD.filter(_.contains("o"))
    //tmpRdd.saveAsTextFile("/Users/zhaogj/tmp/spark/output")
  }
}
