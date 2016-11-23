package com.qzt360.wjPlat

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhaogj on 23/10/2016.
  */
object countMac {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("findMac")//.setMaster("local[*]")
    val sc = new SparkContext(conf)
    val baMac = sc.textFile("/user/zhaogj/output/baMac").cache()
    val kkMac = sc.textFile("/user/zhaogj/output/kkMac").cache()
    val wjMac = sc.textFile("/user/zhaogj/output/wjMac").cache()
    println("baMac count:" + baMac.count() + ", kkMac count:" + kkMac.count() + ", wjMac count:" + wjMac.count() + "\nbaMac intersection kkMac count:" + baMac.intersection(kkMac).count() + "\nbaMac intersection wjMac count:" + baMac.intersection(wjMac).count() + "\nkkMac intersection wjMac count:" + kkMac.intersection(wjMac).count() + "\nbaMac intersection kkMac intersection wjMac count:" + baMac.intersection(kkMac).intersection(wjMac).count())

  }
}
