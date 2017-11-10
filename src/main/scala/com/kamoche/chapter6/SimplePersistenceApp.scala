package com.kamoche.chapter6

import akka.actor.{ActorSystem, Props}

object SimplePersistenceApp extends App {

  val system = ActorSystem("Hello-ActorPersistence")
  val persistenceActor1 = system.actorOf(Props[SamplePersistenceActor], name = "persistent-actor")

  persistenceActor1 ! UserUpdate("baby driver", Add)
  persistenceActor1 ! UserUpdate("zootopia", Add)
  persistenceActor1 ! UserUpdate("boss baby", Add)
  persistenceActor1 ! "snap"
  persistenceActor1 ! "print"
  persistenceActor1 ! UserUpdate("zootopia", Remove)
  persistenceActor1 ! "print"
  val persistenceActor2 = system.actorOf(Props[SamplePersistenceActor])
  persistenceActor2 ! "print"
  Thread.sleep(5000)
  system.terminate()

}
