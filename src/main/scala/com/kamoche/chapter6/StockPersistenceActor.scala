package com.kamoche.chapter6

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}

class StockPersistenceActor(stockId: String) extends PersistentActor with ActorLogging {

  var state = StockHistory()

  def updateState(stockEvent: StockEvent) = state = state.update(stockEvent)

  override def receiveRecover = {
    case stockEvent: StockEvent => updateState(stockEvent)
    case SnapshotOffer(_, snapshot: StockHistory) => state = snapshot
    case RecoveryCompleted => log.info(s"Recovery Completed, current stockState ${state}")
  }

  override def receiveCommand = {
    case ValueUpdate(newValue) => persist(ValueAppended(StockValue(newValue)))(updateState)
    case Snap => saveSnapshot(state)
    case "print" => log.info(s"Current stockState ${state}")
  }

  override def persistenceId = stockId
}

object StockPersistenceActor {
  def props(stockId: String) = Props(new StockPersistenceActor(stockId))

  val name = "stockActor"
}
