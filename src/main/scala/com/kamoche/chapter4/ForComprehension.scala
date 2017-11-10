package com.kamoche.chapter4

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object ForComprehensions extends App {

  val future1 = Future(20 + 30)
  val future2 = Future(40 + 5)
  println(s"Future1 is ${Await.result(future1, 1 seconds)}")
  println(s"Future2 is ${Await.result(future2, 1 seconds)}")
  val finalFuture: Future[Int] = for {
    a <- future1
    b <- future2
  } yield a + b

  println(s"FutureFinal result is ${Await.result(finalFuture, 1 seconds)}")


}
