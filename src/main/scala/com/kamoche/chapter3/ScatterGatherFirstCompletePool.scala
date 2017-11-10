package com.kamoche.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.ScatterGatherFirstCompletedPool
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._



class ScatterGatherFirstCompletedPoolActor extends Actor {
  override def receive = {
    case msg: String => println("I say hi back to you")
    case _ => println("Uknown message")

  }
}

object ScatterGatherFirstCompletedPoolActor {


  def props() = Props(new ScatterGatherFirstCompletedPoolActor)
}

object ScatterGatherFirstCompletePoolTest extends App {

  val system = ActorSystem("hello-ScatterGatherFCPool")
  val router = system.actorOf(ScatterGatherFirstCompletedPool(5, within = 10 seconds).props(ScatterGatherFirstCompletedPoolActor.props()))
  implicit val timeout = Timeout(10 seconds)

  println(Await.result((router ? "hello").mapTo[String], 10 seconds))
}


