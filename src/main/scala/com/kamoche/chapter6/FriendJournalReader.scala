package com.kamoche.chapter6

import akka.actor.ActorSystem
import akka.persistence.Recovery
import akka.persistence.query.PersistenceQuery
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

import scala.concurrent.duration._

object FriendJournalReader extends App {
  implicit val system = ActorSystem("PQuerySystem")

  import system.dispatcher

  implicit val mat = ActorMaterializer()(system)

  val queries = PersistenceQuery(system).readJournalFor[LeveldbReadJournal](LeveldbReadJournal.Identifier)

  val sharon = system.actorOf(FriendActor.props("Sharon", Recovery()))
  val adrian = system.actorOf(FriendActor.props("Adrian", Recovery()))

  sharon ! AddFriend(Friend("Kamoche"))
  sharon ! AddFriend(Friend("Adrian"))
  adrian ! AddFriend(Friend("Kamoche"))
  adrian ! AddFriend(Friend("Sharon"))
  adrian ! AddFriend(Friend("Mary"))

  system.scheduler.scheduleOnce(5 seconds, adrian, AddFriend(Friend("Cynthia")))
  system.scheduler.scheduleOnce(5 seconds, adrian, RemoveFriend(Friend("Mary")))

  Thread.sleep(2000)

  queries.currentPersistenceIds().map(id => system.log.info(s"Id received [$id]")).to(Sink.ignore).run()
  queries.eventsByPersistenceId("Sharon").map(e => log(e.persistenceId, e.event)).to(Sink.ignore).run()
  queries.eventsByPersistenceId("Adrian").map(e => log(e.persistenceId, e.event)).to(Sink.ignore).run()

  def log(id: String, evt: Any) = system.log.info(s"Id ${id}, Event ${evt}")
}
