import sbt.Keys._

lazy val commonSettings = Seq(
  organization := "org.after90",
  version := "0.1.0",
  scalaVersion := "2.10.6"
)

resolvers += Resolver.mavenLocal
resolvers ++= Seq(
  "cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos/"
)


val spark = "org.apache.spark" % "spark-core_2.10" % "1.5.0-cdh5.5.0"
val mllib = "org.apache.spark" % "spark-mllib_2.10" % "1.5.0-cdh5.5.0"
val hdfs = "org.apache.hadoop" % "hadoop-hdfs" % "2.6.0-cdh5.5.0"
val spark_streaming_kafka = "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.5.0-cdh5.5.0"

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "sbtProjectTest",
    libraryDependencies += spark,
    libraryDependencies += mllib,
    libraryDependencies += hdfs,
    libraryDependencies += spark_streaming_kafka
  )
lazy val hello = taskKey[Unit]("一个task事例")