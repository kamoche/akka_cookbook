package com.kamoche.chapter1and2

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object Message {

  case class Done(randomNumber: Int)

  case class GiveMeRandomNumber()

  case class GenerateNumebr()

}

class RandomNumberGeneratorActor extends Actor {

  import Message._
  import scala.util.Random._

  override def receive = {
    case GiveMeRandomNumber => {
      println(s"${self.path.name} receive a message to generate random number from ${sender.path}")
      val randomNumber = nextInt
      sender() ! Done(randomNumber)
    }
    case _ => println("unknown message")
  }
}

object RandomNumberGeneratorActor {
  def props() = Props(new RandomNumberGeneratorActor)

  def name = "numberGenerator"
}

class QueryActor(actorRef: ActorRef) extends Actor {

  import Message._

  override def receive = {
    case GenerateNumebr => {
      println(s" ${self.path.name} request a randomNumber from ${actorRef.path}")
      actorRef ! GiveMeRandomNumber

    }
    case Done(randomNumber) => {
      println(s"${self.path.name} receive randomNumber: ${randomNumber} from ${sender.path}")
    }

  }
}

object QueryActor {
  def props(actorRef: ActorRef) = Props(new QueryActor(actorRef))

  def name = "queryActor"
}

object ActorsCommunicationTest extends App {

  import Message._

  val system = ActorSystem("ActorCommunication")
  val numberGeneratorActor = system.actorOf(RandomNumberGeneratorActor.props(), RandomNumberGeneratorActor.name)
  val queryActor = system.actorOf(QueryActor.props(numberGeneratorActor), QueryActor.name)

  queryActor ! GenerateNumebr

}
