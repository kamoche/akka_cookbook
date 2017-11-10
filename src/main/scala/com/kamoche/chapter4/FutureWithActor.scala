package com.kamoche.chapter4

import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask

import scala.concurrent.Await

case class Sum(a:Int ,b: Int)
case class Response[+A](response: A)
class FutureWithActor extends Actor{
  override def receive = {
    case Sum(a,b) => sender() ! (a + b)
  }
}

object FutureWithActor {
  def props=Props(new FutureWithActor)
  def name="calculation"
}


object FutureWithActorTest extends App{
  val system=ActorSystem("Hello-Akka")
  implicit val timeout=Timeout(10 seconds)
  val calculator=system.actorOf(FutureWithActor.props,FutureWithActor.name)

  val future=(calculator ? Sum(1,3)).mapTo[Int]
  val sum=Await.result(future, 10 seconds)
  println(s"ActorFuture result of sum(1,3):  $sum")
}
