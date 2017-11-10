package com.kamoche.chapter6

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, Recovery, RecoveryCompleted, SnapshotOffer}

class FriendActor(friendId: String, recovery: Recovery) extends PersistentActor with ActorLogging {

  override def persistenceId = friendId

  var state = FriendState()

  def updateState(friendEvent: FriendEvent) = state = state.update(friendEvent)

  override def receiveRecover = {
    case evt: FriendEvent => {
      log.info(s"Replaying event: $evt")
      updateState(evt)
    }
    case SnapshotOffer(_, recoveredState: FriendState) => {
      log.info(s"Snapshot offered: $recoveredState")
      state = recoveredState
    }
    case RecoveryCompleted => log.info(s"Recovery completed. Current State: $state")
  }

  override def receiveCommand = {
    case AddFriend(friend) => persist(FriendAdded(friend))(updateState)
    case RemoveFriend(friend) => persist(FriendRemoved(friend))(updateState)
    case "snap" => saveSnapshot(state)
    case "print" => log.info(s"Current state: $state")
  }

}

object FriendActor {
  def props(friendId: String, recovery: Recovery) = Props(new FriendActor(friendId, recovery))

  def name = "friendsActor"
}
