package com.kamoche.chapter5

import akka.actor.ActorSystem
import scala.concurrent.duration._


object ScheduleOperation extends App{

  val system=ActorSystem("Hello-Scheduler")

  import system.dispatcher
  system.scheduler.scheduleOnce(10 seconds){
    println(s"Product of (4 * 5 is ${4 *5}" )
  }

  system.scheduler.schedule(11 seconds, 2 seconds){
    println("Scheduled to run after 2 seconds")
  }
}
