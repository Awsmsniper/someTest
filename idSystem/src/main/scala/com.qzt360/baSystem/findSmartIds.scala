package com.qzt360.baSystem

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhaogj on 18/11/2016.
  */
object findSmartIds {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("findSmartIds") //.setMaster("local[*]")
    val sc = new SparkContext(conf)
    //备案系统产生的日志中，分析智能关联的身份
    //同一mac上产生的身份应该是同一使用人的
    val authLog = sc.textFile("/user/aWifi/authLog/authLog.*.txt") //.repartition(1);
    val macTel = authLog.filter(x => {
      val parts = (x + "\tendmark").split("\t")
      (parts.length == 35)
    }).map { x => {
      val parts = (x + "\tmark").split("\t")
      //mac,tel,1020004
      (parts(19), parts(5) + ",1020004")
    }
    }

    val netIdLog = sc.textFile("/user/aWifi/netIdLog/netIdLog.*.txt") //.repartition(1)
    val macNetid = netIdLog.filter { x => {
      val parts = (x + "\tmark").split("\t")
      (parts.length == 9) && (parts(0).length == 17) && (parts(0).split("-").length == 6)
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      //mac,netid,netidType
      (parts(0), parts(3) + "," + parts(2))
    }
    }
    (macTel union macNetid).distinct().reduceByKey(_ + ";" + _).saveAsTextFile("/user/zhaogj/output/baSystemSmartIds")

  }
}
