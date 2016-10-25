package com.qzt360

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhaogj on 21/10/2016.
  */
object findNetId {
  def main(args: Array[String]): Unit = {
    var strInput = "/Users/zhaogj/tmp/baSystem/netIdLog.1476962205697.txt"
    if (args.length == 1) {
      strInput = args(0)
    }
    val conf = new SparkConf().setAppName("findNetId").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val netIdLog = sc.textFile(strInput)
    val netId = netIdLog.filter { x => {
      var result = false;
      val parts = x.split("\t")
      if (parts.length == 8) {
        result = true
      }
      result
    }
    }.map { x => {
      val parts = x.split("\t")
      parts(2) + "\t" + parts(3)
    }
    }
    println("netId count: " + netId.distinct().count())
  }
}
