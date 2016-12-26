lazy val commonSettings = Seq(
  organization := "org.after90",
  version := "0.1.0",
  scalaVersion := "2.10.6"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "sbtProjectTest"
  )