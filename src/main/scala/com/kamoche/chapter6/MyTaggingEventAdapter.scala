package com.kamoche.chapter6

import akka.persistence.journal.{Tagged, WriteEventAdapter}

class MyTaggingEventAdapter(tags: Set[String]) extends WriteEventAdapter {
  override def manifest(event: Any) = ""

  override def toJournal(event: Any) = Tagged(event, tags)
}
