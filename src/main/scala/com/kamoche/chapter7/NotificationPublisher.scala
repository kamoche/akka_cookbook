package com.kamoche.chapter7

import akka.actor.{Actor, Props}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish

class NotificationPublisher extends Actor{

  val mediator=DistributedPubSub(context.system).mediator
  override def receive = {
    case notification:Notification =>{
      mediator ! Publish("notification",notification)
    }
  }
}

object NotificationPublisher {
  def props()=Props(new NotificationPublisher())
  val name="notificationPublisher"
}
