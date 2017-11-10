package com.kamoche.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.BalancingPool


class BalancingPoolActor extends Actor{
  override def receive = {
    case msg: String => println(s"Hello $msg ${self.path}")
    case _ => println("Unknown message")
  }
}

object BalancingPoolActor {
  def props()=Props(new BalancingPoolActor).withRouter(new BalancingPool(10))
  def name="balancingRoute"
  //share same mailbox
}

object BalancingPoolTest extends App{

  val system =ActorSystem("Hello-BalancingPool")
  val balancingPoolActor=system.actorOf(BalancingPoolActor.props(), BalancingPoolActor.name)

  (1 to 1000).foreach{
    x => balancingPoolActor ! s"message $x"
  }
}
