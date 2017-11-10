package com.kamoche.chapter1and2

import java.util.concurrent.ConcurrentLinkedQueue

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.dispatch.{Envelope, MailboxType, MessageQueue, ProducesMessageQueue}
import com.typesafe.config.Config


class CustomMessageQueue extends MessageQueue {

  val queue = new ConcurrentLinkedQueue[Envelope]()

  override def enqueue(receiver: ActorRef, handle: Envelope): Unit = {
    if (handle.sender.path.name == "MyActor") {
      handle.sender ! "Hi you, got your message, am processing it"
      queue.offer(handle)
    } else {
      handle.sender ! "Am afraid i wont process your message since i dont know you"
    }
  }

  override def dequeue(): Envelope = queue.poll()

  override def numberOfMessages: Int = queue.size()

  override def hasMessages: Boolean = !queue.isEmpty

  override def cleanUp(owner: ActorRef, deadLetters: MessageQueue): Unit = {
    while (hasMessages) {
      deadLetters.enqueue(owner, dequeue())
    }
  }
}

class CustomUnboundedMailBox extends MailboxType with ProducesMessageQueue[CustomMessageQueue] {
  def this(setting: ActorSystem.Settings, config: Config) = {
    this()
  }

  override def create(owner: Option[ActorRef], system: Option[ActorSystem]) = new CustomMessageQueue
}

class SpecialActor extends Actor {
  override def receive = {
    case msg: String => println(s"msg is $msg")
  }
}

class MyActor extends Actor {
  override def receive = {
    case (msg: String, actorRef: ActorRef) => {
      actorRef ! msg
    }
    case msg => println(msg)
  }
}

object CustomMailboxTest extends App {
  val system = ActorSystem("CustomMailBox")
  val specialActor = system.actorOf(Props[SpecialActor].withDispatcher("custom-dispatcher"))
  val actor = system.actorOf(Props[MyActor], "strange")
  val actor1 = system.actorOf(Props[MyActor], "MyActor")

  actor ! ("Ping", specialActor)
  actor1 ! ("Pong", specialActor)
}
