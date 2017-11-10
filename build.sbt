name := "Hello-Akka"

version := "1.0"

scalaVersion := "2.12.2"

//mainClass in assembly := Some("com.kamoche.chapter5.ScheduleActor")

lazy val actorVersion = "2.5.6"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % actorVersion,
  "com.typesafe.akka" %% "akka-agent" % actorVersion,
  "com.typesafe.akka" %% "akka-testkit" % actorVersion,
  "com.typesafe.akka" %% "akka-persistence" % actorVersion,
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
  "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.55",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % Runtime,
  "com.hootsuite" %% "akka-persistence-redis" % "0.8.0",
  "com.typesafe.akka" %% "akka-persistence-query" % actorVersion,
  "com.typesafe.akka" %% "akka-remote" % actorVersion,
  "com.typesafe.akka" %% "akka-cluster" % actorVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % actorVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding" % actorVersion,
  "com.typesafe.akka" %% "akka-distributed-data" % actorVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

