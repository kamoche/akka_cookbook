package com.kamoche.chapter4

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object AddFuture extends App {

  val future = Future {
    1 + 2
  }
  val sum = Await.result(future, 10 seconds)
  println(s"Future reult is $sum")
}
