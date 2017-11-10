package com.kamoche.chapter3

import akka.actor.{Actor, ActorSystem, PoisonPill, Props}
import akka.routing.{Broadcast, RandomPool}

case object Handle

class SpecialHandledActor extends Actor {
  override def receive = {
    case Handle => println(s"${self.path.name} said hello")
  }
}

object SpecialHandledActor {
  def props = Props(new SpecialHandledActor).withRouter(RandomPool(5))
  def name="specialHandled"
}

object SpecialHandledTest extends App {
  val system = ActorSystem("Hello-akka")
  val specialHandledActor = system.actorOf(SpecialHandledActor.props, SpecialHandledActor.name)

  specialHandledActor ! Broadcast(Handle)
  specialHandledActor ! Broadcast(PoisonPill)
  specialHandledActor ! Handle
}

