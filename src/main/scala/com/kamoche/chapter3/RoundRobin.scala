package com.kamoche.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RoundRobinPool

class RoundRobin {

}


class  RoundRobinActor extends Actor{
    override def receive = {
    case msg:String => println(s"Hello $msg ${self.path.name}")
    case _ => println("Unknown message")
  }
}

object RoundRobinActor {
  def props=Props(new RoundRobinActor).withRouter(new RoundRobinPool(6))
}


object RoundRobinTest extends App{

  val system=ActorSystem("Hello-RoundRobin")
  val roundRobinActor=system.actorOf(RoundRobinActor.props)

  (1 to 12).foreach{
    x => roundRobinActor ! s"message $x"
  }
}
