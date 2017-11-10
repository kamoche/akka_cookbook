package com.kamoche.chapter5

import akka.actor.{Actor, ActorSystem, Props}
import scala.concurrent.duration._

case object Add

class RandomIntAdder extends Actor {

  import scala.util.Random._

  override def receive = {
    case Add => {
      val randomInt1 = nextInt(10)
      val randomInt2 = nextInt(10)
      println(s"Sum of randomInt $randomInt1 and $randomInt2 is ${randomInt1 + randomInt2}")
    }
  }
}

object ScheduleActor extends App {
  val system = ActorSystem("Hello-Akka")

  import system.dispatcher

  val randomIntAdderActor = system.actorOf(Props[RandomIntAdder], name = "randomIntAdder")
  system.scheduler.scheduleOnce(10 seconds, randomIntAdderActor, Add)
  system.scheduler.schedule(11 seconds, 2 second, randomIntAdderActor, Add)

}
