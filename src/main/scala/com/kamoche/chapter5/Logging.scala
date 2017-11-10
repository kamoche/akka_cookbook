package com.kamoche.chapter5

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.kamoche.chapter4.Sum


class LoggingActor extends Actor with ActorLogging {

  override def receive = {
    case Sum(a, b) => log.info(s"Sum of ${a} and ${b} is ${(a + b)}")
    case msg => log.warning("Unknown message")
  }
}

object LoggingTest extends App {
  val system = ActorSystem("Hello-Logging")
  val actor = system.actorOf(Props[LoggingActor], name = "SumActor")

  actor ! Sum(2, 4)
  actor ! "Do you want coffee"
  system.terminate()
}
