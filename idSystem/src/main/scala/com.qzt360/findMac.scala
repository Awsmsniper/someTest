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
      val parts = (x + "\tmark").split("\t")
      if (parts.length == 35) {
        result = true
      }
      result
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      parts(19)
    }
    }

    val netLog = sc.textFile("/user/aWifi/netLog")
    val netLogMac = netLog.filter { x => {
      var result = false;
      val parts = (x + "\tmark").split("\t")
      if (parts.length == 23) {
        result = true
      }
      result
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      parts(15)
    }
    }

    val netIdLog = sc.textFile("/user/aWifi/netIdLog")
    val netIdLogMac = netIdLog.filter { x => {
      var result = false;
      val parts = (x + "\tmark").split("\t")
      if (parts.length == 9) {
        if (parts(0).length == 17 && parts(0).split("-").length == 6) {
          result = true
        }
      }
      result
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      parts(0)
    }
    }

    val baMac = (authLogMac union netLogMac union netIdLogMac).distinct()

    //卡口平台
    // /user/wjpt/20161022/tmac_1477065600_b8x3ak.ok

    val tMacLog = sc.textFile("/user/wjpt/*/tmac_*")
    val tMacMac = tMacLog.filter { x => {
      var result = false;
      val parts = (x + "\tmark").split("\t")
      if (parts.length == 18) {
        if (parts(0).length == 17 && parts(0).split("-").length == 6) {
          result = true
        }
      }
      result
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      parts(0)
    }
    }

    val kkMac = tMacMac.distinct()

    // /user/wjpt/20161022/hotspot_1477065600_5p97p2.ok
    // ap信息不做分析

    // /user/wjpt/20161022/wlan_12_1477065600_j9rm5w.ok
    // /user/wjpt/20161022/wlan_1477065600_g0wapf.ok
    // wlan是什么数据没搞清楚，暂不分析


    //网监平台
    // /user/wjpt/20161022/authlog_19_1477065600_54p1j3.ok
    // /user/wjpt/20161022/ftp_1477065600_eumvgh.ok
    // /user/wjpt/20161022/guestinfo_1477065600_9qma90.ok
    // /user/wjpt/20161022/http_1477065600_c5g62k.ok
    // /user/wjpt/20161022/olgame_1477065600_nwcv45.ok
    // /user/wjpt/20161022/post_1477065600_phc39j.ok
    // /user/wjpt/20161022/webSearch_1477065600_ei6n1a.ok

    //网监平台目录中不只是企智通产生的数据

    // /user/wjpt/20161022/email_1477065600_htgbft.ok
    val emailLog = sc.textFile("/user/wjpt/*/email_*")
    val emailMac = emailLog.filter { x => {
      var result = false;
      val parts = (x + "\tmark").split("\t")
      if (parts.length == 31) {
        if (parts(6).length == 17 && parts(6).split(":").length == 6) {
          result = true
        }
      }
      result
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      parts(6).replaceAll(":", "-")
    }
    }

    // /user/wjpt/20161022/im_1477065600_i4a9km.ok
    val imLog = sc.textFile("/user/wjpt/*/im_*")
    val imMac = imLog.filter { x => {
      var result = false;
      val parts = (x + "\tmark").split("\t")
      if (parts.length == 23) {
        if (parts(6).length == 17 && parts(6).split(":").length == 6) {
          result = true
        }
      }
      result
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      parts(6).replaceAll(":", "-")
    }
    }

    // /user/wjpt/20161022/weibo_1477065600_84lksv.ok
    val weiboLog = sc.textFile("/user/wjpt/*/weibo_*")
    val weiboMac = weiboLog.filter { x => {
      var result = false;
      val parts = (x + "\tmark").split("\t")
      if (parts.length == 25) {
        if (parts(7).length == 17 && parts(7).split(":").length == 6) {
          result = true
        }
      }
      result
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      parts(7).replaceAll(":", "-")
    }
    }

    // /user/wjpt/20161022/netidinfo_1477065600_989b23.ok

    val netIdLogwj = sc.textFile("/user/wjpt/*/netidinfo_*")
    val netIdMacwj = netIdLogwj.filter { x => {
      var result = false;
      val parts = (x + "\tmark").split("\t")
      if (parts.length == 19) {
        if (parts(17).length == 17 && parts(17).split(":").length == 6) {
          result = true
        }
      }
      result
    }
    }.map { x => {
      val parts = (x + "\tmark").split("\t")
      parts(17).replaceAll(":", "-")
    }
    }

    val wjMac = (emailMac union imMac union weiboMac union netIdMacwj).distinct()

    baMac.saveAsTextFile("/user/zhaogj/output/baMac")
    kkMac.saveAsTextFile("/user/zhaogj/output/kkMac")
    wjMac.saveAsTextFile("/user/zhaogj/output/wjMac")
    //println("baMac count:" + baMac.count() + ", kkMac count:" + kkMac.count() + ", wjMac count:" + wjMac.count() + "\nbaMac intersection kkMac count:" + baMac.intersection(kkMac).count() + "\nbaMac intersection wjMac count:" + baMac.intersection(wjMac).count() + "\nkkMac intersection wjMac count:" + kkMac.intersection(wjMac).count() + "\nbaMac intersection kkMac intersection wjMac count:" + baMac.intersection(kkMac).intersection(wjMac).count())

  }

}
