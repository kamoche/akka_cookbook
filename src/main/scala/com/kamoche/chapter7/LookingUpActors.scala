package com.kamoche.chapter7

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.util.Success


object LookingUpActorSelection extends App {
  val config = ConfigFactory.load("application-remote")
  val system = ActorSystem("LookingUpActors", config)
  implicit val dispatcher = system.dispatcher
  val actorSelection = system.actorSelection("akka.tcp://LookingUpRemoteActor@127.0.0.1:10002/user/remoteActor")

  actorSelection ! "Despicable me 3"

  actorSelection.resolveOne(3 seconds).onComplete {
    case Success(value) => {
      println("I got an actorRef")
      value ! "test"
    }
    case _ => println("Failed")

  }

}

object LookingUpRemoteActor extends App {

  val config = ConfigFactory.load("application-rc")
  val system = ActorSystem("LookingUpRemoteActor", config)
  system.actorOf(Props[SimpleActor], name = "remoteActor")
}
