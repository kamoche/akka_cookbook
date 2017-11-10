package com.kamoche.chapter5

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory




class WordCountActor extends Actor{
  override def receive = {
    case msg:String => println(msg.size)
    case _=> println("Unknown message")
  }
}


object ActorWithConfigTest extends App{
  val config: Config = ConfigFactory.load("akka.conf")
  val system=ActorSystem(config.getString("myactor.actorsystem"))
  val countActor=system.actorOf(Props[WordCountActor],config.getString("myactor.name"))
  println(countActor.path)
  countActor ! "baa baa black sheep"

  system.terminate()

}
