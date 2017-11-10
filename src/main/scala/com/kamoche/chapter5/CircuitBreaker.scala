package com.kamoche.chapter5

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.CircuitBreaker
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._


case class FetchEmployee(empNo: String)

case class Employee(empNo: String, name: String, department: String)

object DB {
  val employeesDetails = Map(
    "a115829" -> Employee("a115829", "Ken", "IT"),
    "a115839" -> Employee("a115839", "Julius", "IT"),
    "a131825" -> Employee("a131825", "Nyengo", "IT"),
    "a131826" -> Employee("a131826", "Max", "IT"),
    "a131827" -> Employee("a131827", "Esther", "Finance"),
    "a135828" -> Employee("a135828", "Cathrine", "DIT"),
    "a115828" -> Employee("a115828", "Kamoche", "IT"),
    "a181828" -> Employee("a181828", "Ben", "CLN")
  )

}

class DBActor extends Actor {
  override def receive = {
    case FetchEmployee(empNo) => {
      if (empNo.startsWith("a13182"))
        Thread.sleep(3000)
      else sender ! DB.employeesDetails.get(empNo)
    }
  }
}

object CircuitBreakerApp extends App {

  val system = ActorSystem("Hello-circuitBreaker")
  implicit val ec = system.dispatcher
  implicit val timeout = Timeout(3 seconds)

  val dbbreaker =
    new CircuitBreaker(
      system.scheduler,
      maxFailures = 2,
      callTimeout = 1 second,
      resetTimeout = 2 seconds
    ).onOpen(notifyMeOnOpen).onClose(notifyMeOnClose)

  def notifyMeOnOpen: Unit = {
    println("============Db circuitBreaker is now open, we will not close for two seconds==============")
  }

  def notifyMeOnClose: Unit = {
    println("==========Db circuitBreaker is now closed===================")
  }

  val dBActor = system.actorOf(Props[DBActor], name = "dbActor")
  val empNoList = List("a115829","a115839","a131825", "a131826", "a131827", "a135828", "a115828", "a181828","b21921","c282831")

  empNoList.map {
    empNo => {
      Thread.sleep(3000)
      val empFuture = dbbreaker.withCircuitBreaker(dBActor ? FetchEmployee(empNo))
      empFuture.map(empDetails =>
        s"Employee details is ${empDetails}, with empNo: $empNo").recover({
        case fail => s"Failed: user details for ID: $empNo can not be retrieved at the moment, try again later " + fail.toString
      }).foreach {
        x => println(x)
      }
    }
  }

}
