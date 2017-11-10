package com.kamoche.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.ConsistentHashingPool
import akka.routing.ConsistentHashingRouter.{ConsistentHashMapping, ConsistentHashable, ConsistentHashableEnvelope}



case class Evict(key: String)

case class Get(key: String) extends ConsistentHashable {
  override def consistentHashKey: Any = key
}

case class Entry(key: String, value: String)

class Cache extends Actor {
  var cache = Map.empty[String, String]

  override def receive = {
    case Entry(key, value) => {
      println(s"${self.path.name} adding key $key")
      cache += (key -> value)
    }
    case Get(key) => {
      println(s" ${self.path.name} retriving key $key")
      sender() ! cache.get(key)
    }
    case Evict(key) => {
      println(s"${self.path.name} evicting key $key")
      cache -= key
    }

  }
}

object Cache {

  def hashMapping: ConsistentHashMapping = {
    case Evict(key) => key
  }

  def props = Props(new Cache).withRouter(ConsistentHashingPool(10, hashMapping = hashMapping))

  def name = "cache"
}

object ConsistentHashingPoolTest extends App {

  val system = ActorSystem("Hello-ConsistentHPooling")
  val cache = system.actorOf(Cache.props, Cache.name)

  cache ! ConsistentHashableEnvelope(message = Entry("sbt", "SBT in Action"), hashKey = "sbt")
  cache ! ConsistentHashableEnvelope(message = Entry("scala", "Programming in scala"), hashKey = "sbt")

  cache ! Get("sbt")
  cache ! Get("scala")
  cache ! Evict("sbt")
}
