package com.kamoche.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.TailChoppingPool
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._


class TailChoppingPoolActor extends Actor {
  override def receive = {
    case msg: String => sender ! "Hey too"
    case _ => println("Unknown message")
  }
}

object TailChoppingPoolActor {
  def props() = Props(new TailChoppingPoolActor).withRouter(TailChoppingPool(5, within = 10.seconds, interval = 20.millis))

}

object TailChoppingPoolTest extends App {

  val system = ActorSystem("Hello-TailChoppingPool")
  implicit val timeout = Timeout(10 seconds)
  val router = system.actorOf(TailChoppingPoolActor.props())

  println(Await.result((router ? "hello").mapTo[String], 10 seconds))
}
