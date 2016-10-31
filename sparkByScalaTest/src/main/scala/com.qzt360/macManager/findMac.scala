package com.qzt360.macManager

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, FileUtil, Path}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by zhaogj on 25/10/2016.
  */
object findMac {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("findMac")
    val sc = new SparkContext(conf)
    //解决读入文件是大量小文件的效率问题
    //    sc.hadoopConfiguration.set("dfs.replication", "1")
    //    sc.hadoopConfiguration.setLong("mapreduce.input.fileinputformat.split.maxsize", 1024 * 1024 * 128)
    //    sc.hadoopConfiguration.setLong("mapreduce.input.fileinputformat.split.minsize", 1024 * 1024 * 128)

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
    authLogMac.repartition(4).distinct().saveAsTextFile("/user/zhaogj/output/authLogMac")

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
    netLogMac.repartition(4).distinct().saveAsTextFile("/user/zhaogj/output/netLogMac")

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
    netIdLogMac.repartition(4).distinct().saveAsTextFile("/user/zhaogj/output/netIdLogMac")

    val baMac = (sc.textFile("/user/zhaogj/output/authLogMac/part*") union sc.textFile("/user/zhaogj/output/netLogMac/part*") union sc.textFile("/user/zhaogj/output/netIdLogMac/part*")).repartition(4).distinct()

    //卡口平台
    // /user/wjpt/20161022/tmac_1477065600_b8x3ak.ok

    val tMacLog = sc.textFile(getFileList("/user/wjpt/", "tmac_").mkString(","))
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
    tMacMac.repartition(4).distinct().saveAsTextFile("/user/zhaogj/output/tMacMac")

    val kkMac = sc.textFile("/user/zhaogj/output/tMacMac/part*").repartition(4).distinct()

    // /user/wjpt/20161022/hotspot_1477065600_5p97p2.ok
    // ap信息不做分析

    // /user/wjpt/20161022/wlan_12_1477065600_j9rm5w.ok
    // /user/wjpt/20161022/wlan_1477065600_g0wapf.ok
    // wlan是什么数据没搞清楚，暂不分析


    //网监平台

    // /user/wjpt/20161022/email_1477065600_htgbft.ok
    val emailLog = sc.textFile(getFileList("/user/wjpt/", "email_").mkString(","))
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
    emailMac.repartition(4).distinct().saveAsTextFile("/user/zhaogj/output/emailMac")

    // /user/wjpt/20161022/im_1477065600_i4a9km.ok
    val imLog = sc.textFile(getFileList("/user/wjpt/", "im_").mkString(","))
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
    imMac.repartition(4).distinct().saveAsTextFile("/user/zhaogj/output/imMac")

    // /user/wjpt/20161022/weibo_1477065600_84lksv.ok
    val weiboLog = sc.textFile(getFileList("/user/wjpt/", "weibo_").mkString(","))
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
    weiboMac.repartition(4).distinct().saveAsTextFile("/user/zhaogj/output/weiboMac")

    // /user/wjpt/20161022/netidinfo_1477065600_989b23.ok

    val netIdLogwj = sc.textFile(getFileList("/user/wjpt/", "netidinfo_").mkString(","))
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
    netIdMacwj.repartition(4).distinct().saveAsTextFile("/user/zhaogj/output/netIdMacwj")

    val wjMac = (sc.textFile("/user/zhaogj/output/emailMac/part*") union sc.textFile("/user/zhaogj/output/imMac/part*") union sc.textFile("/user/zhaogj/output/weiboMac/part*") union sc.textFile("/user/zhaogj/output/netIdMacwj/part*")).repartition(4).distinct()

    baMac.saveAsTextFile("/user/zhaogj/output/baMac")
    kkMac.saveAsTextFile("/user/zhaogj/output/kkMac")
    wjMac.saveAsTextFile("/user/zhaogj/output/wjMac")

  }

  def getFileList(logPath: String, logPrefix: String): (ArrayBuffer[String]) = {
    var listFile = ArrayBuffer[String]()
    val hadoopConf = new Configuration()
    //找到所有.har结尾的文件
    val listPath = FileUtil.stat2Paths(FileSystem.get(hadoopConf).listStatus(new Path(logPath)))
    for (path <- listPath) {
      if (path.getName.endsWith(".har")) {
        //逐个处理.har文件
        val pathTmp = new Path("har://" + logPath + path.getName)
        val listPathHar = FileUtil.stat2Paths(pathTmp.getFileSystem(hadoopConf).listStatus(pathTmp))
        for (pathHar <- listPathHar) {
          if (pathHar.getName.startsWith(logPrefix)) {
            listFile += ("har://" + logPath + path.getName + "/" + pathHar.getName)
          }
        }
      } else {
        //非har文件，直接看看文件夹里的文件名
        val pathTmp = new Path(logPath + path.getName)
        val listPathTmp = FileUtil.stat2Paths(pathTmp.getFileSystem(hadoopConf).listStatus(pathTmp))
        for (pathLog <- listPathTmp) {
          if (pathLog.getName.startsWith(logPrefix)) {
            listFile += (logPath + path.getName + "/" + pathLog.getName)
          }
        }
      }
    }
    listFile
  }
}
