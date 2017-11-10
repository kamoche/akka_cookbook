package com.kamoche.chapter7

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class ScalingOutApplication {

}

object ScalingOutWorkerApp extends App {

  val system = ActorSystem("WorkerActorSystem")
  implicit val dispatcher = system.dispatcher
  val selection = system.actorSelection("akka.tcp://MasterActorSystem@127.0.0.1:10001/user/masterActor")
  selection.resolveOne(3 seconds).onComplete {
    case Success(actorRef) => {
      println("We got the ActorRef for masterActor")
      val workerActorPool = system.actorOf(WorkerActor.props(), WorkerActor.name)
      actorRef ! RegisterWorker(workerActorPool)
    }
    case Failure(e) => println(s"Error: $e, reason: ${e.getMessage} ")
  }
}

object ScalingOutMasterApp extends App {

  val config = ConfigFactory.load("application-remote")
  val system = ActorSystem("MasterActorSystem", config)
  val masterWorker = system.actorOf(MasterWorker.props(), MasterWorker.name)

  (1 to 1000).foreach {
    i =>
      masterWorker ! Work(s"work_$i")
      Thread.sleep(1000)
  }
}

