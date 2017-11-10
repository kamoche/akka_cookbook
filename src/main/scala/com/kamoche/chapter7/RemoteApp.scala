package com.kamoche.chapter7

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory


object RemoteApp extends App {
  val config = ConfigFactory.load("application-remote")
  val system = ActorSystem("RemoteSystem", config)

}

object RemoteApp2 extends App {
  val config = ConfigFactory.load("application-rc")
  val system = ActorSystem("RemoteSystem1", config)
  println("Creating Actor for Remote 2")
  val simpleActor = system.actorOf(Props[SimpleActor], name = "simpleRemoteActor")
  simpleActor ! "It takes time to make a good wine"

}