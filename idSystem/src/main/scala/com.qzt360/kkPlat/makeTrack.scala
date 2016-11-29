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
      val parts = (x + "\tmark").split("\t")
      //字段数合法
      if (parts.length == 18) {
        //采集到的mac合法
        if (parts(0).length == 17 && parts(0).split("-").length == 6) {
          true
        } else {
          false
        }
      } else {
        false
      }
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      //strEqpId,strMac,strTime
      parts(14) + "," + parts(0) + "," + parts(3)
    }
    }.distinct().saveAsTextFile("/user/zhaogj/output/track")
  }
}
