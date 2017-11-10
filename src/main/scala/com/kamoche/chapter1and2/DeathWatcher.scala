package com.kamoche.chapter1and2

import akka.actor.{Actor, ActorSystem, Props, Terminated}



case object Service

case object Kill

class ServiceActor extends Actor {
  override def receive = {
    case Service => println("I offer special services")
  }
}
class DeathWatcherActor extends Actor{
  val child=context.actorOf(Props[ServiceActor],"serviceActor")
  context.watch(child)
  override def receive = {
    case Service => child ! Service
    case Kill => context.stop(child)
    case Terminated(`child`) => println("The service Actor has been terminated and its no longer available")
  }
}
object DeathWatcherTest extends App {

  val system=ActorSystem("hello-akka")
  val deathWatcherActor= system.actorOf(Props[DeathWatcherActor], "deathWatcher")
  deathWatcherActor ! Service
  deathWatcherActor ! Kill
  deathWatcherActor ! Service //message sent to deathletter

}
