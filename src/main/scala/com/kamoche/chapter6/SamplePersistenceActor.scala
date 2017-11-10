package com.kamoche.chapter6

import akka.persistence.{PersistentActor, SnapshotOffer}

class SamplePersistenceActor extends PersistentActor {

  override def persistenceId = "unique-id-1"

  var state = ActiveUsers()

  def updateState(event: Event) = state = state.update(event)

  override def receiveRecover: Receive = {
    case event: Event => updateState(event)
    case SnapshotOffer(_, snapshot: ActiveUsers) => state = snapshot
  }

  override def receiveCommand: Receive = {
    case UserUpdate(userId, Add) => persist(AddUserEvent(userId))(updateState)
    case UserUpdate(userId, Remove) => persist(RemoveUserEvent(userId))(updateState)
    case "snap" => saveSnapshot(state)
    case "print" => println(state)
    case ShutdownPersistentActor => context.stop(self)
  }

  override def postStop(): Unit = {
    println(s"Stopping ${self.path}")
  }
}
