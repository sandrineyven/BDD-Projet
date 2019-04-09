name := "projetdm2BDD"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.0.3"
libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.12"
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.3.0" exclude("org.apache.hadoop", "hadoop-yarn-server-web-proxy")
libraryDependencies += "org.apache.spark" % "spark-graphx_2.11" % "2.3.0" exclude("org.apache.hadoop", "hadoop-yarn-server-web-proxy")
libraryDependencies += "com.miglayout" % "miglayout-swing" % "4.2"
