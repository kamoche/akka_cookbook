package com.kamoche.chapter1and2

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class FibonacciActor extends Actor {

  def fib(num: Int): Int = num match {
    case 0 | 1 => num
    case _ => fib(num - 1) + fib(num - 2)
  }

  override def receive = {
    case num: Int =>
      val fibonacciNumber = fib(num)
      sender() ! fibonacciNumber
  }

}

object FibonacciActor {
  def props() = Props(new FibonacciActor)

  def name = "fibo"
}


object FibonacciActorTest extends App {

  val system = ActorSystem("Math")
  val fiboActor = system.actorOf(FibonacciActor.props(), FibonacciActor.name)
  implicit val timeout = Timeout(10 second)

  val future = (fiboActor ? 10).mapTo[Int]

  val fibonacciNumber = Await.result(future, 10 seconds)
  println(fibonacciNumber)
}
