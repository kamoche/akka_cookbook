package com.kamoche.chapter7

import akka.actor.{Actor, Props}
import akka.routing.RoundRobinPool

case class Work(wordId: String)

case class WorkDone(workId: String)

class WorkerActor extends Actor {
  override def receive = {
    case Work(wordId) => {
      Thread.sleep(1000) //processing work
      println(s"Work ${wordId} is been  processed by worker actor")
      sender() ! WorkDone(wordId)
    }
  }
}

object WorkerActor {
  def props() = Props(new WorkerActor).withRouter(new RoundRobinPool(10))

  val name = "workerActor"
}
