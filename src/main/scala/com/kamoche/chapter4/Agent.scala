package com.kamoche.chapter4

import akka.agent._
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


object AgentApp extends App{
  val timeout=Timeout(10 seconds)
  val agent=Agent(10)
  val result=agent.get()
  println(s"Result is now $result")
  val f1:Future[Int]=agent alter 13
  println(s"Result after sending a value ${Await.result(f1,10 seconds)}")
  val f2:Future[Int]=agent alter (_ + 7)
  println(s"Result after sending a funtion ${Await.result(f2,10 seconds)}")

}


