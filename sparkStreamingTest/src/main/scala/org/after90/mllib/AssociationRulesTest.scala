package org.after90.mllib

import org.apache.spark.mllib.fpm.AssociationRules
import org.apache.spark.mllib.fpm.FPGrowth.FreqItemset
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhaogj on 23/10/2016.
  */
object AssociationRulesTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("findMac").setMaster("local[*]")
    val sc = new SparkContext(conf)
    /*
    val freqItemsets = sc.parallelize(Seq(
      new FreqItemset(Array("a"), 15L),
      new FreqItemset(Array("b"), 35L),
      new FreqItemset(Array("a", "b"), 12L),
      new FreqItemset(Array("c"), 12L),
      new FreqItemset(Array("a", "c"), 12L)
    ))

    val ar = new AssociationRules()
      .setMinConfidence(0.3)
    val results = ar.run(freqItemsets)

    results.collect().foreach { rule =>
      println("[" + rule.antecedent.mkString(",")
        + "=>"
        + rule.consequent.mkString(",") + "]," + rule.confidence)
    }
    */
    /*
    val datasetFile = sc.textFile("/Users/zhaogj/tmp/spark/mllib/ar.txt")
    val freqItemsets = datasetFile.map(x => {
      val parts = x.split("\t")
      if (parts.length == 2) {
        new FreqItemset(Array(parts(0)), 15L)
      } else if (parts.length == 3) {
        new FreqItemset(Array(parts(0), parts(1)), 15L)
      }
    })
    */
    /*
    val transactions = sc.parallelize(Seq(
      Array("a", "b", "e"),
      Array("c", "b", "e", "f"),
      Array("a", "b", "c"),
      Array("c", "e", "f"),
      Array("d", "e", "f")
    ))
*/
    val transactions = sc.textFile("/Users/zhaogj/tmp/spark/mllib/ar.txt").map(x => {
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

    results.collect().foreach { rule =>
      println("[" + rule.antecedent.mkString(",")
        + "=>"
        + rule.consequent.mkString(",") + "]," + rule.confidence)
    }

  }
}
