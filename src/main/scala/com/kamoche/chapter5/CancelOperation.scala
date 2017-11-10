package com.kamoche.chapter5

import akka.actor.{Actor, ActorSystem, Cancellable, Props}
import scala.concurrent.duration._

case object StartActivity

class CancelOperation extends Actor {
  var n = 10

  override def receive = {
    case StartActivity => {
      println(s"Do some activities as long as n > 0, current n count= ${n}")
      n -= 1
      if (n == 0) Scheduler.cancellable.cancel()
    }
  }
}

object Scheduler extends App {
  val system = ActorSystem("Hello-akka")
  val actor = system.actorOf(Props[CancelOperation], name = "cancelOperation")

  import system.dispatcher

  val cancellable: Cancellable = system.scheduler.schedule(0 seconds, 2 seconds, actor, StartActivity)
}
