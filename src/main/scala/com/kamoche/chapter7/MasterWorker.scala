package com.kamoche.chapter7

import akka.actor.{Actor, ActorRef, Props, Terminated}

import scala.util.Random

case class RegisterWorker(workerActor: ActorRef)

class MasterWorker extends Actor {
  var workers = List.empty[ActorRef]

  override def receive = {
    case RegisterWorker(workerActer) => {
      context.watch(workerActer)
      workers = workerActer :: workers
    }
    case Terminated(actorRef) => {
      println(s"Actor ${actorRef.path.address} have been terminated. Removing it from worker list")
      workers = workers.filterNot(_ == actorRef)
    }
    case work: Work if workers.isEmpty => {
      println(s"Work ${work.wordId} cannot be processed since there is no active worker actor ${sender.path.address.hostPort}")
    }
    case work: Work => {
      workers(Random.nextInt(workers.size)) ! work
    }
    case WorkDone(workId) => {
      println(s"Finished processing work ${workId}")
    }

  }
}

object MasterWorker {
  def props() = Props(new MasterWorker())

  val name = "masterActor"
}
