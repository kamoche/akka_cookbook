package com.kamoche.chapter5

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.kamoche.chapter4.Sum
import org.scalatest.{MustMatchers, WordSpecLike}
import util.ShutDownSystemAfterAll

class SumActorTest extends TestKit(ActorSystem("SumActorTest")) with ImplicitSender with WordSpecLike with MustMatchers with ShutDownSystemAfterAll {

  "Sum Actor" must {
    "send back the sum of two integer" in {
      val sumActor = system.actorOf(Props[SumActor], name = "sumActor")
      sumActor ! Sum(3, 7)
      expectMsg(10)
    }
  }
}
