package com.kamoche.chapter1and2

import akka.actor.{Actor, ActorSystem, Props}
import akka.dispatch.ControlMessage

object ControlAwareMailboxTest extends App{

  val system=ActorSystem("Hello-controlAware")
  val controlAwareActor=system.actorOf(Props[Logger].withDispatcher("control-aware-dispatcher"), "control-aware")
  controlAwareActor ! "Top"
  controlAwareActor ! "Ten"
  controlAwareActor ! "watched movies 2017"
  controlAwareActor ! MyControlMessage
}

object MyControlMessage extends ControlMessage

class Logger extends Actor {

  override def receive = {
    case MyControlMessage => {
      println("Damn, controlMessage need to be processed first")
    }
    case x => println(x.toString)
  }
}
