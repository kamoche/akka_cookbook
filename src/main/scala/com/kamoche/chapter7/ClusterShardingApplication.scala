package com.kamoche.chapter7

import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import com.kamoche.chapter7.TemperatureActor.{GetCurrentTemperature, Location, UpdateTemperature}

import scala.util.{Failure, Success}

object ClusterShardingApplication extends App {
  val actorSystem = ActorSystem("ClusterSystem")

  import actorSystem.dispatcher

  val temperatureActor: ActorRef = ClusterSharding(actorSystem).start(
    typeName = TemperatureActor.shardName,
    entityProps = TemperatureActor.props(),
    settings = ClusterShardingSettings(actorSystem),
    extractEntityId = TemperatureActor.extractEntityId,
    extractShardId = TemperatureActor.extractShardId)
  //Let's simulate some time has passed. Never use  Thread.sleep in production !
  Thread.sleep(30000)
  val locations = Vector(Location("USA", "Chicago"), Location("ESP", "Madrid"), Location("FIN", "Helsinki"))
  temperatureActor ! UpdateTemperature(locations(0), 1.0)
  temperatureActor ! UpdateTemperature(locations(1), 20.0)
  temperatureActor ! UpdateTemperature(locations(2), -10.0)

  implicit val timeout = Timeout(5 seconds)
  locations.foreach {
    case location =>
      (temperatureActor ? GetCurrentTemperature(location)).onComplete {
        case Success(value) => {
          println(s"Current temperature in $location is $value")
        }
        case Failure(e) =>
          println(s"Error: ${e}, reason: ${e.getMessage}")

      }
  }
}