package com.kamoche.chapter1and2

import akka.actor.ActorSystem

object TestActorSystem  extends App{

  val system=ActorSystem("HelloActor")
  println(system)
}
