package com.kamoche.chapter6


case class Friend(id: String)

//command
case class AddFriend(friend: Friend)

case class RemoveFriend(friend: Friend)

//events note* happened in the past
sealed trait FriendEvent

case class FriendAdded(friend: Friend) extends FriendEvent

case class FriendRemoved(friend: Friend) extends FriendEvent

//state
case class FriendState(friends: Vector[Friend] = Vector.empty[Friend]) {

  def update(event: FriendEvent) = event match {
    case FriendAdded(friend) => copy(friends :+ friend)
    case FriendRemoved(friend) => copy(friends.filterNot(_ == friend))
  }

  override def toString: String = friends.mkString(",")
}
