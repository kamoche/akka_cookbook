package com.kamoche.chapter6

import akka.actor.{ActorSystem, PoisonPill, Props}

object SafePersistenceActorShutdownApp extends App {
  val system = ActorSystem("Hello-ActorPersistence")
  val persistenceActor1 = system.actorOf(Props[SamplePersistenceActor], name = "persistent-actor")

  persistenceActor1 ! UserUpdate("baby driver", Add)
  persistenceActor1 ! UserUpdate("zootopia", Add)
  persistenceActor1 ! UserUpdate("boss baby", Add)
  persistenceActor1 ! UserUpdate("zootopia", Remove)
  persistenceActor1 ! PoisonPill
  val persistenceActor2 = system.actorOf(Props[SamplePersistenceActor])
  persistenceActor2 ! UserUpdate("zootopia", Add)
  persistenceActor2 ! UserUpdate("boss baby", Add)
  persistenceActor2 ! ShutdownPersistentActor
  Thread.sleep(5000)
  system.terminate()
}
