package com.kamoche.chapter1and2

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorRef, ActorSystem, AllForOneStrategy, Props, SupervisorStrategy}


case class Add(a: Int, b: Int)

case class Sub(a: Int, b: Int)

case class Div(a: Int, b: Int)

case object Start


class Calculator(printer: ActorRef) extends Actor {
  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    println("Calculator: Restarting due to ArithmeticException")
  }

  override def receive = {
    case Add(a, b) => printer ! s"sum is ${a + b}"
    case Sub(a, b) => printer ! s"subtraction is ${a - b}"
    case Div(a, b) => printer ! s"division is ${a / b}"
  }
}

object Calculator {
  def props(actorRef: ActorRef) = Props(new Calculator(actorRef))

  def name = "Calculator"
}

class Printer extends Actor {

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)
    println("Printer, i am restarting too")
  }

  override def receive = {
    case msg => println(msg)
  }
}

object Printer {
  def props() = Props(new Printer)

  def name = "logger"
}

class OneForAllStrategySupervision extends Actor {

  import scala.concurrent.duration._

  override def supervisorStrategy: SupervisorStrategy = AllForOneStrategy(maxNrOfRetries = 10, 1 second) {
    case _: ArithmeticException => Restart
    case _: NullPointerException => Resume
    case _: IllegalArgumentException => Stop
    case _: Exception => Escalate

  }

  val childPrinter = context.actorOf(Printer.props(), Printer.name)
  val childCal = context.actorOf(Calculator.props(childPrinter), Calculator.name)

  override def receive = {
    case Start => childCal ! Add(1, 1)
      childCal ! Sub(10, 1)
      childCal ! Div(10, 2)
      childCal ! Div(10, 0)
      childCal ! Sub(10, 2)
  }
}


object OneForAllSupervisionStrategyTest extends App {

  val system = ActorSystem("AllForOneS")

  val supervisor = system.actorOf(Props[OneForAllStrategySupervision], "calSupervisor")

  supervisor ! Start
}
