package org.after90.test

/**
  * Created by zhaogj on 21/10/2016.
  */

import org.apache.spark.{SparkConf, SparkContext}

object MyFirstApp {
  def main(args: Array[String]): Unit = {
    println("say love me.")
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val textRDD = sc.textFile("/Users/zhaogj/tmp/spark/sparkinput.txt")
    println(textRDD.count())
    val tmpRdd = textRDD.filter(_.contains("o"))
    println(tmpRdd.count())
  }
}
