package com.kamoche.chapter4

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Callback extends App {
  val future = Future(2 + 5.5).mapTo[Double]

  future onComplete {
    case Success(value) => println(value)
    case Failure(x) => println(s"Failed: $x")
  }

  println("Get Executed before callback")


}
