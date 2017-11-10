package com.kamoche.chapter7

import akka.actor.{Actor, ActorRef, Props}
import com.kamoche.chapter7.ChatServer.Disconnect

import scala.io.StdIn._

object ChatClientInterface{
  case object Check
  def props(actorRef: ActorRef)= Props(new ChatClientInterface(actorRef))
  val name="chatClientInterface"
}

class ChatClientInterface(chatClient: ActorRef) extends Actor{
  import ChatClientInterface._

  override def preStart(): Unit = {
    println("You are logged in. Type and press Enter to send message or type 'DISCONNECT' to log out")
    self ! Check
  }
  override def receive = {
    case Check =>{
      readLine() match {
        case "DISCONNECT"  =>
          chatClient ! Disconnect
          println("Disconnecting ......")
          context.stop(self)
        case msg =>
          chatClient ! msg
          self ! Check
      }
    }
  }
}
