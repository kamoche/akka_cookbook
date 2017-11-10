package com.kamoche.chapter7

import akka.actor
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.util.{Failure, Success}

object ChatClientApplication extends App {

//  val config = ConfigFactory.load("application-4")
  val system = ActorSystem("ChatServerSystem")
  implicit val dispatcher = system.dispatcher
  val chatServerAddress = "akka.tcp://ChatServerSystem@127.0.0.1:10003/user/chatServer"

  system.actorSelection(chatServerAddress).resolveOne(3 seconds).onComplete {
    case Success(chatServer) =>
      val chatClient = system.actorOf(ChatClient.props(chatServer), ChatClient.name)
      system.actorOf(ChatClientInterface.props(chatClient), ChatClientInterface.name)
    case Failure(e) => println(s"Error: ${e}, reason: ${e.getMessage}")
  }

}

object ChatServerApplication extends App {

  val config = ConfigFactory.load("application-3")
  val system = ActorSystem("ChatServerSystem", config)
  val chatServerActor = system.actorOf(ChatServer.props, ChatServer.name)
}
