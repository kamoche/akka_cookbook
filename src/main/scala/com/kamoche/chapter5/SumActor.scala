package com.kamoche.chapter5

import akka.actor.Actor
import com.kamoche.chapter4.Sum

class SumActor extends Actor{
  override def receive = {
    case Sum(a,b) => sender ! (a + b)
  }
}
