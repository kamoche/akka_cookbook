package com.kamoche.chapter6

import akka.actor.ActorSystem

trait AkkaHelper {
  lazy val system = ActorSystem("StockSystem")
  lazy val stockActor = system.actorOf(StockPersistenceActor.props("stockID"), StockPersistenceActor.name)
}

object StockApp extends App with AkkaHelper {
  stockActor ! ValueUpdate(200.0)
  stockActor ! ValueUpdate(250.0)
  stockActor ! "print"
  Thread.sleep(10000)
  system.terminate()
}

object StockRecoveryApp extends App with AkkaHelper {
  stockActor ! ValueUpdate(510.5)
  stockActor ! "print"
  Thread.sleep(10000)
  system.terminate()
}

//sbt -Dconfig.resource=application-cassandra.conf  -Dcom.datastax.driver.USE_NATIVE_CLOCK=false "runMain com.kamoche.chapter6.StockRecoveryApp"
//sbt -Dconfig.resource=application-redis.conf  -Dcom.datastax.driver.USE_NATIVE_CLOCK=false "runMain com.kamoche.chapter6.StockRecoveryApp"
