package com.qzt360.kkPlat

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhaogj on 29/11/2016.
  */
object someTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("makeTrack")
    val sc = new SparkContext(conf)
    val tMacLog = sc.textFile("/user/wfpt/qzt/tmac_2016112*").distinct()
    //val tMacLog = sc.textFile("/user/wfpt/qzt/tmac_20161120")

    tMacLog.count()

    tMacLog.filter { x => {
      val parts = (x + "\tmark").split("\t")
      //字段数合法
      if (parts.length == 18) {
        false
      } else {
        true
      }
    }
    }.count()

    tMacLog.filter { x => {
      val parts = (x + "\tmark").split("\t")
      //字段数合法
      if (parts.length == 18) {
        //采集到的mac合法
        if (parts(0).length == 17 && parts(0).split("-").length == 6) {
          //设备id合法
          false
        } else {
          true
        }
      } else {
        false
      }
    }
    }.count()

    tMacLog.filter { x => {
      val parts = (x + "\tmark").split("\t")
      //字段数合法
      if (parts.length == 18) {
        //采集到的mac合法
        if (parts(0).length == 17 && parts(0).split("-").length == 6) {
          //设备id合法
          if (parts(8).length == 21 || (parts(8).length == 17 && parts(8).split("-").length == 6)) {
            false
          } else {
            true
          }
        } else {
          false
        }
      } else {
        false
      }
    }
    }.distinct().saveAsTextFile("/user/zhaogj/output/errEqpId")

    tMacLog.filter { x => {
      val parts = (x + "\tmark").split("\t")
      //字段数合法
      if (parts.length == 18) {
        //采集到的mac合法
        if (parts(0).length == 17 && parts(0).split("-").length == 6) {
          //设备id合法
          if (parts(8).length == 21 || (parts(8).length == 17 && parts(8).split("-").length == 6)) {
            true
          } else {
            false
          }
        } else {
          false
        }
      } else {
        false
      }
    }
    }.distinct().saveAsTextFile("/user/zhaogj/output/okEqpId")

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
      //strApMac,strMac,strTime
      //parts(15) + "," + parts(0) + "," + parts(3)
      parts(14)
    }
    }.distinct().count()
  }
}
