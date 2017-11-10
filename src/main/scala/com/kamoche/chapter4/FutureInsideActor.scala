package com.kamoche.chapter4

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class FutureActor extends Actor {

  import context.dispatcher

  override def receive = {
    case Sum(a, b) => {
      val future = Future {
        a + b
      }
      //      println(Await.result(future, 10 seconds)) //not recommended

      future.onComplete {
        case Success(value) => println(value)
        case Failure(_) => println("Failed")
      }
    }

  }
}

object FutureActor {
  def props = Props(new FutureActor)

  def name = "futureWithinActor"
}

object FutureInsideActorTest extends App {
  val system = ActorSystem("Hello-akka")
  val futureActor = system.actorOf(FutureActor.props, FutureActor.name)
  futureActor ! Sum(1, 5)
}
