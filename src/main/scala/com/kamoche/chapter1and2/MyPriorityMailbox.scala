package com.kamoche.chapter1and2

import akka.actor.{Actor, ActorSystem, Props}
import akka.dispatch.{PriorityGenerator, UnboundedPriorityMailbox}
import com.typesafe.config.Config

class MyPriorityActor extends Actor {
  override def receive: PartialFunction[Any, Unit] = {
    case x: Int => println(x)
    case x: String => println(x)
    case x: Long => println(x)
    case x => println(x)
  }
}

class MyPriorityActorMailBox(setting: ActorSystem.Settings, config: Config) extends UnboundedPriorityMailbox(PriorityGenerator {
  case x: Int => 1
  case x: String => 0
  case x: Long => 2
  case x => 3
})

object MyPriorityMailboxTest extends App{

  val system=ActorSystem("HelloPriority")
  val priorityActor=system.actorOf(Props[MyPriorityActor].withDispatcher("prio-dispatcher"))
  priorityActor ! 27
  priorityActor ! 27.0
  priorityActor ! "kamoche"
  priorityActor ! "jackson"
  priorityActor ! 2L
  priorityActor ! 28

}


