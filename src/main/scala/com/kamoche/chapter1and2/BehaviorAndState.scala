package com.kamoche.chapter1and2

import akka.actor.{Actor, ActorSystem, Props}

class SummingActor(initialSum: Int) extends Actor {

  var sum = 0 //state inside actor
  //behavior applied on the state
  override def receive = {
    case x: Int =>
      print(s"Current State sum: $sum and constructor value => initialSum: $initialSum new state after $sum + $initialSum  + $x is:  " )
      sum = initialSum + sum + x
      println(s"$sum")
    case o => println("Unknown message")
  }
}

object SummingActor {
  def props(initialSum: Int) = Props(new SummingActor(initialSum))

  def name = "summingActor"
}

object BehaviorAndState extends App {

  val actorSystem = ActorSystem("HelloActor")
  val summingActor = actorSystem.actorOf(SummingActor.props(10), SummingActor.name)
  println(summingActor.path)

  (1 to 10).foreach{
    x => summingActor ! x
  }

}



