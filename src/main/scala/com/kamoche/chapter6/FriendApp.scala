package com.kamoche.chapter6

import akka.actor.ActorSystem
import akka.persistence.{Recovery, SnapshotSelectionCriteria}


object FriendApp extends App {

  val system = ActorSystem("Hello-Friend")
  val friendActor = system.actorOf(FriendActor.props("Kamoche_2863", Recovery()))

  friendActor ! AddFriend(Friend("Joel"))
  friendActor ! AddFriend(Friend("Godi"))
  friendActor ! AddFriend(Friend("Ambrose"))
  friendActor ! AddFriend(Friend("Sharon"))
  friendActor ! AddFriend(Friend("Benten"))
  friendActor ! "snap"
  friendActor ! RemoveFriend(Friend("Benten"))
  friendActor ! AddFriend(Friend("Vector"))
  friendActor ! "print"
  Thread.sleep(5000)
  system.terminate()


}

object FriendRecoveryDefault extends App {
  val system = ActorSystem("Hello-Friend")
  val friendActor = system.actorOf(FriendActor.props("Kamoche_2863", Recovery()))
  friendActor ! "print"
  Thread.sleep(3000)
  system.terminate()

}

object FriendRecoveryOnlyEvents extends App {
  val system = ActorSystem("Hello-Friend")
  val recovery = Recovery(fromSnapshot = SnapshotSelectionCriteria.None)
  val friendActor = system.actorOf(FriendActor.props("Kamoche_2863", recovery))
//  friendActor ! "print"
  Thread.sleep(3000)
  system.terminate()
}

object FriendRecoveryEventsSequence extends App {
  val system = ActorSystem("Hello-Friend")
  val recovery = Recovery(fromSnapshot = SnapshotSelectionCriteria.None, toSequenceNr = 2)
  val friendActor = system.actorOf(FriendActor.props("Kamoche_2863", recovery))
  Thread.sleep(2000)
  system.terminate()
}

object FriendRecoveryEventsReplay extends App {
  val system = ActorSystem("Hello-Friend")
  val recovery = Recovery(fromSnapshot = SnapshotSelectionCriteria.None, replayMax = 3)
  val friendActor = system.actorOf(FriendActor.props("Kamoche_2863", recovery))
  Thread.sleep(2000)
  system.terminate()
}


