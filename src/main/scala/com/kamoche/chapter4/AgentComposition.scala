package com.kamoche.chapter4

import akka.agent.Agent

import scala.concurrent.ExecutionContext.Implicits.global

object AgentComposition extends App {

  val agent1 = Agent(30)
  val agent2 = Agent(50)

  val finalAgent = for {
    a <- agent1
    b <- agent2
  } yield a + b

  println(finalAgent.get())

}
