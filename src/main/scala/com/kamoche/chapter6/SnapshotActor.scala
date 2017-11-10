package com.kamoche.chapter6

import akka.actor.ActorLogging
import akka.persistence._

case object Snap

class SnapshotActor extends PersistentActor with ActorLogging {

  var state = ActiveUsers()

  def updateState(event: Event) = state = state.update(event)

  override def receiveRecover = {
    case evt: Event => updateState(evt)
    case SnapshotOffer(metadata, snapshot: ActiveUsers) => state = snapshot
    case RecoveryCompleted => log.info(s"Recovery completed. Current state: ${state}")
  }

  override def receiveCommand = {
    case UserUpdate(userId, Add) => persist(AddUserEvent(userId))(updateState)
    case UserUpdate(userId, Remove) => persist(RemoveUserEvent(userId))(updateState)
    case Snap => saveSnapshot(state)
    case SaveSnapshotSuccess(metadata) => log.info(s"Snapshot success: ${metadata}")
    case SaveSnapshotFailure(metadata, e) => log.error(s"Snapshot Failed: ${metadata}, Reason : ${e}")
  }

  override def persistenceId = "ss-id-1"

  override def postStop(): Unit = log.info("Stopping")

  override def recovery: Recovery = {
    Recovery(SnapshotSelectionCriteria.Latest)
  }
}
