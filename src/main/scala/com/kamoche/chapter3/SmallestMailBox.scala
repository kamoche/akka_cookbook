package com.kamoche.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.SmallestMailboxPool

class SmallestMailBox{

}

class SmallestMailBoxActor extends Actor{
  override def receive = {
    case msg: String => println(s"Hello $msg ${self.path.name}")
    case _ => println("Unknown message")
  }
}

object SmallestMailBoxActor {
  def props()=Props(new SmallestMailBoxActor)
  def name="smallestMailbox"
}

object smallestMailBoxTest extends App{
  val system=ActorSystem("Hello-akka")
  val router=system.actorOf(SmallestMailboxPool(5).props(SmallestMailBoxActor.props()))

  (1 to 30).foreach{
    x => router ! s"message $x"
  }
}
