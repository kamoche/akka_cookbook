package com.kamoche.chapter1and2

import akka.actor._
import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._


case object Error

case class StopActor(actorRef: ActorRef)

class LifeCycleActor extends Actor {
  var sum = 1

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)
    println(s"sum in preRestart is $sum")
  }

  override def preStart(): Unit = {
    println(s"sum in preStart is $sum")
  }

  override def postStop(): Unit = {
    println(s"sum in postStop is ${sum * 3}")
  }

  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    sum = sum * 2
    println(s"sum in postRestart is ${sum}")

  }

  override def receive = {
    case Error => throw new ArithmeticException()
    case x=> println(s"default msg: ${x}")
  }
}

class Supervisor extends Actor {
  override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: ArithmeticException => Restart
    case t => super.supervisorStrategy.decider.applyOrElse(t, (_: Any) => Escalate)
  }

  override def receive = {
    case (props: Props, actorName: String) => {
      sender ! context.actorOf(props, actorName)
    }
    case StopActor(actorRef) => context.stop(actorRef)
  }
}


object ActorLifeCycleTest extends App {

  val system = ActorSystem("Supervision")
  implicit val timeout = Timeout(2 second)
  val supervisor = system.actorOf(Props[Supervisor], name = "Supervision")

  val childFuture = supervisor ? (Props(new LifeCycleActor), "lifeCycleActor")

  val child = Await.result(childFuture.mapTo[ActorRef], 2 second)

  (1 to 15).foreach { x =>
    child ! Error
    child ! s"Ping Number $x"
    Thread.sleep(1000)
  }




  Thread.sleep(2000)
//  supervisor ! StopActor(child)

}