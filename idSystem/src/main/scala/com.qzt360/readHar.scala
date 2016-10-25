package com.qzt360

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, FileUtil, Path}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by zhaogj on 24/10/2016.
  */
object readHar {
  def main(args: Array[String]): Unit = {
    val logPath = "/user/wjpt/"
    val logPrefix = "tmac_"
    val sparkConf = new SparkConf().setAppName("readHar")
    val sc = new SparkContext(sparkConf)

    val listFile = getFileList(logPath, logPrefix)
    val tMacLogRdd = sc.textFile(listFile.mkString(","))
    println(tMacLogRdd.count())
    
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
