package com.kamoche.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}

class ResizablePool {

}

case object Load

class LoadActor extends Actor {
  override def receive = {
    case Load => println(s"${self.path} Handing load of request")
  }
}

object LoadActor {
  val resizer = DefaultResizer(lowerBound = 2, upperBound = 15)

  def props = Props(new LoadActor).withRouter(RoundRobinPool(5, Some(resizer)))

  def name = "loadResizerActor"
}

object ResizablePoolTest extends App {
  val system = ActorSystem("Hello-LoadResize")
  val loadActor = system.actorOf(LoadActor.props, LoadActor.name)

  (1 to 1000).foreach {
    x => loadActor ! Load
  }
  loadActor ! "Hello"
}
