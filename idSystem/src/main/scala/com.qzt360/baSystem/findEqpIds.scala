package com.qzt360.baSystem

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhaogj on 18/11/2016.
  * 汇总身份出现在哪个设备上
  */
object findEqpIds {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("findSmartIds") //.setMaster("local[*]")
    val sc = new SparkContext(conf)
    //备案系统产生的日志中，分析智能关联的身份
    //同一mac上产生的身份应该是同一使用人的
    val authLog = sc.textFile("/user/aWifi/authLog/authLog.*.txt") //.repartition(1);
    val telEqpid = authLog.filter(x => {
      val parts = (x + "\tendmark").split("\t")
      (parts.length == 35)
    }).map { x => {
      val parts = (x + "\tmark").split("\t")
      //tel,1020004 eqpMac
      (parts(5) + ",1020004", parts(21))
    }
    }

    val netIdLog = sc.textFile("/user/aWifi/netIdLog/netIdLog.*.txt") //.repartition(1)
    val netIdEqpid = netIdLog.filter { x => {
      val parts = (x + "\tmark").split("\t")
      (parts.length == 9) && (parts(0).length == 17) && (parts(0).split("-").length == 6) && (parts(5).length == 21)
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      val strEqpMac = parts(5).substring(9, 11) + "-" + parts(5).substring(11, 13) + "-" + parts(5).substring(13, 15) + "-" + parts(5).substring(15, 17) + "-" + parts(5).substring(17, 19) + "-" + parts(5).substring(19, 21)
      //netid,netidType eqpMac
      (parts(3) + "," + parts(2), strEqpMac)
    }
    }


    (telEqpid union netIdEqpid).distinct().reduceByKey(_ + ";" + _).saveAsTextFile("/user/zhaogj/output/baSystemSmartIds")

  }
}
