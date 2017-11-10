package com.kamoche.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RandomPool


class RandomPoolActor extends Actor{
  override def receive = {
    case msg: String => println(s"I am ${self.path.name}, received $msg")
    case _ => println("Unknown message")
  }
}

object RandomPoolActor {
  def props=Props(new RandomPoolActor).withRouter(RandomPool(5))
  def name="randomPool"
}

object RandPollTest extends App{

  val system=ActorSystem("Hello-RandomPool")
  val randomPoolActor=system.actorOf(RandomPoolActor.props, RandomPoolActor.name)
  (1 to 5).foreach{
    x=> randomPoolActor ! s"message ${x}"
  }
}
