package com.qzt360

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhaogj on 21/10/2016.
  * 网监，备案，卡口中采集到的MAC提取，计算重合情况如何
  */
object findMac {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("findMac").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //备案系统中的mac
    val authLog = sc.textFile("/user/aWifi/authLog")
    val authLogMac = authLog.filter { x => {
      var result = false;
      val parts = x.split("\t")
      if (parts.length == 34) {
        result = true
      }
      result
    }
    }.map { x => {
      val parts = x.split("\t")
      parts(19)
    }
    }.distinct()
    //authLogMac.saveAsTextFile("/user/zhaogj/authLogMac")

    val netLog = sc.textFile("/user/aWifi/netLog")
    val netLogMac = netLog.filter { x => {
      var result = false;
      val parts = x.split("\t")
      if (parts.length == 21) {
        result = true
      }
      result
    }
    }.map { x => {
      val parts = x.split("\t")
      parts(15)
    }
    }.distinct()
    //netLogMac.saveAsTextFile("/user/zhaogj/netLogMac")

    val netIdLog = sc.textFile("/user/aWifi/netIdLog")
    val netIdLogMac = netIdLog.filter { x => {
      var result = false;
      val parts = x.split("\t")
      if (parts.length == 8) {
        if (parts(0).length == 17 && parts(0).split("-").length == 6) {
          result = true
        }
      }
      result
    }
    }.map { x => {
      val parts = x.split("\t")
      parts(0)
    }
    }.distinct()

    netIdLogMac.saveAsTextFile("/user/zhaogj/netIdLogMac")
  }

}