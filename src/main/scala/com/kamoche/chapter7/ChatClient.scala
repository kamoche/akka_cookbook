package com.kamoche.chapter7

import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import com.kamoche.chapter7.ChatServer.{Disconnect, Disconnected, Message}
import akka.pattern.ask
import akka.pattern.pipe

import scala.concurrent.duration._

object ChatClient {
  def props(chatServer: ActorRef) = Props(new ChatClient(chatServer))

  val name = "chatClientActor"
}

class ChatClient(chatServer: ActorRef) extends Actor {
  implicit val dispatcher = context.dispatcher
  implicit val timeout = Timeout(3 seconds)

  override def receive = {
    case Disconnect => {
      (chatServer ? Disconnect).pipeTo(self)
    }
    case Disconnected => {
      context.stop(self)
    }
    case body: String => {
      chatServer ! Message(self, body)
    }
    case msg: Message =>
      println(s"Message from [${msg.author}] at[ ${msg.creationTimestamp}]: ${msg.body}")
  }
}
