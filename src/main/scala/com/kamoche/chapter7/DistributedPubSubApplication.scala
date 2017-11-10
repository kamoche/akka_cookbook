package com.kamoche.chapter7

import akka.actor.ActorSystem
import akka.cluster.Cluster

import scala.util.Random

object DistributedPubSubApplication extends App {

  val clusterSystem = ActorSystem("ClusterSystem")
  val cluster = Cluster(clusterSystem)
  val notificationPublisher = clusterSystem.actorOf(NotificationPublisher.props(), NotificationPublisher.name)
  val notificationSubscriber = clusterSystem.actorOf(NotificationSubscriber.props(), NotificationSubscriber.name)
  val clusterAddress = cluster.selfUniqueAddress
  val notification = Notification(s"Sent from cluster node ${clusterAddress}", "Ping!")
  implicit val dispatcher = clusterSystem.dispatcher

  import scala.concurrent.duration._

  clusterSystem.scheduler.schedule(Random.nextInt(5) seconds, 5 seconds, notificationPublisher, notification)

}
