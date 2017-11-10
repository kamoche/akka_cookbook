package com.kamoche.chapter4

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object ReducingFuture extends App {

  val listOfFutures = (1 to 10).map(Future(_))
  println(listOfFutures)
  val finalFuture = Future.reduceLeft(listOfFutures)(_ + _)

  finalFuture onComplete {
    case Success(sum) => println(s"sum of number from 1 to 10 is $sum")
    case Failure(e) => println("Failed: $e")
  }
}
