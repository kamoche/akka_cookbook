package com.kamoche.chapter7

import akka.actor.Actor

class SimpleActor extends Actor{
  override def receive = {
    case msg: String => println(s"Received message: ${msg}, i have been created at ${self.path.address.hostPort}")
    case _ => println("Unknown message")
  }
}
