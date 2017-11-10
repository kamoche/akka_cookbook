package com.kamoche.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.BroadcastPool


class BroadCastPoolActor extends Actor{
  override def receive = {
    case msg: String => println(s"Hello $msg ${self.path.name}")
    case _ => println("Unknown message")
  }
}

object BroadCastPoolActor {
  val props=Props(new BroadCastPoolActor).withRouter(new BroadcastPool(2))
}

object BroadCastPoolTest extends App{
    val system=ActorSystem("Hello-BroadcastPool")
    val broadCastPoolActor=system.actorOf(BroadCastPoolActor.props)

  (1 to 4).foreach{
    x=> broadCastPoolActor ! s"message $x"
  }
}
