package com.kamoche.chapter7

import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Subscribe, SubscribeAck}


case class Notification(title: String, body: String)

class NotificationSubscriber extends Actor {
  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe("notification", self)
  val cluster = Cluster(context.system)
  val clusterAddress = cluster.selfUniqueAddress

  override def receive = {
    case notification: Notification => {
      println(s"Got a notification on node: ${clusterAddress} => ${notification}")
    }
    case SubscribeAck(Subscribe("notification", None, `self`)) => println("Subscribing......")
  }
}

object NotificationSubscriber {
  def props() = Props(new NotificationSubscriber())

  val name = "notificationSubscriber"
}
