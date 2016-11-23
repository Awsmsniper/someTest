package com.qzt360.kkPlat

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhaogj on 23/11/2016.
  * 整理tmac数据，得到轨迹信息
  */
object makeTrack {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("makeTrack")
    val sc = new SparkContext(conf)
    val tMacLog = sc.textFile("/user/wfpt/qzt/tmac_*")
    //val tMacLog = sc.textFile("/user/wfpt/qzt/tmac_20161120")
    tMacLog.filter { x => {
      var result = false;
      val parts = (x + "\tmark").split("\t")
      if (parts.length == 18) {
        if (parts(0).length == 17 && parts(0).split("-").length == 6 && parts(8).length == 17 && parts(8).split("-").length == 6) {
          result = true
        }
      }
      result
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      //strApMac,strMac,strTime
      parts(8) + "," + parts(0) + "," + parts(3)
    }
    }.distinct().saveAsTextFile("/user/zhaogj/output/track")
  }
}
