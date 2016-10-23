package com.qzt360

import org.apache.spark.mllib.fpm.AssociationRules
import org.apache.spark.mllib.fpm.FPGrowth.FreqItemset
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhaogj on 23/10/2016.
  * 分析一段时间产生的同行人
  */
object findTogetherMac {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("findTogetherMac").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val tMacLog = sc.textFile("/user/wjpt/201610*/tmac_*")
    //val tMacLog = sc.textFile("/Users/zhaogj/tmp/wjpt/tmac_1477065600_b8x3ak.ok")

    val macHourEqp = tMacLog.filter { x => {
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
      val nTime = parts(3).toInt
      parts(0) + "\t" + (nTime - nTime % (60 * 60)) + "\t" + parts(14)
    }
    }.distinct()

    val macsRdd = macHourEqp.map(x => {
      val parts = x.split("\t")
      (parts(1) + "\t" + parts(2), parts(0))
    }).reduceByKey((x, y) => {
      x + "\t" + y
    }).map { case (timeEqp, macs) => macs }

    val transactions = macsRdd.map(x => {
      x.split("\t")
    })

    val freqItemsets = transactions
      .flatMap(xs =>
        (xs.combinations(1) ++ xs.combinations(2)).map(x => (x.toList, 1L))
      )
      .reduceByKey(_ + _)
      .map { case (xs, cnt) => new FreqItemset(xs.toArray, cnt) }

    val ar = new AssociationRules()
      .setMinConfidence(0.3)
    val results = ar.run(freqItemsets)
    /*
        results.collect().foreach { rule =>
          println("[" + rule.antecedent.mkString(",")
            + "=>"
            + rule.consequent.mkString(",") + "]," + rule.confidence)
        }
    */
    results.map(rule => {
      "[" + rule.antecedent.mkString(",") + "=>" + rule.consequent.mkString(",") + "]:" + rule.confidence
    }).saveAsTextFile("/user/zhaogj/output/togetherMac")
  }
}
