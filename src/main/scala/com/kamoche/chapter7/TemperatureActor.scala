package com.kamoche.chapter7

import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import akka.cluster.sharding.ShardRegion

class TemperatureActor extends Actor {

  import TemperatureActor._

  var temperatureMap = Map.empty[Location, Double]

  override def preStart(): Unit = {
    println(s"Got created at ${Cluster(context.system).selfUniqueAddress}")
  }

  override def receive = {
    case update@UpdateTemperature(location, temp) =>
      temperatureMap += (location -> temp)
    case GetCurrentTemperature(location) =>
      sender() ! temperatureMap(location)
  }
}

object TemperatureActor {

  def props()=Props(new TemperatureActor())

  case class Location(country: String, city: String) {
    override def toString: String = s"${country}:${city}"
  }

  case class UpdateTemperature(location: Location, currentTemp: Double)

  case class GetCurrentTemperature(location: Location)

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case msg@UpdateTemperature(location, _) =>
      (s"$location", msg)
    case msg@GetCurrentTemperature(location) =>
      (s"$location", msg)
  }

  val numberOfShard = 100
  val extractShardId: ShardRegion.ExtractShardId = {
    case UpdateTemperature(location, _) => {
      (s"$location".hashCode % numberOfShard).toString
    }
    case GetCurrentTemperature(location) =>
      (s"$location".hashCode % numberOfShard).toString
  }

  val shardName = "Temperature"
}
