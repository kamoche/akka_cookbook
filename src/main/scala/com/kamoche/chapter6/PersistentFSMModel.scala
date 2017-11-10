package com.kamoche.chapter6

import akka.persistence.fsm.PersistentFSM.FSMState

//State
sealed trait CountDownLatchState extends FSMState

case object Open extends CountDownLatchState {
  val identifier = "Open"
}

case object Closed extends CountDownLatchState {
  val identifier = "Closed"
}

//Data
case class Count(n: Int = -1)

//Command-messages to be send to FSM actor
sealed trait Command

case class Initialize(n: Int) extends Command

case object Mark extends Command

//Event-to be persisted in the journals
sealed trait DomainEvent

case object LatchDownOpen extends DomainEvent

case class LatchDownClosed(remaining: Int) extends DomainEvent


