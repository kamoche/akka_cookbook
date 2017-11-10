package com.kamoche.chapter4


import scala.annotation.tailrec
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object Parallel extends App {

  import Fib._

  val t1 = System.currentTimeMillis()
  val sum = fib(100) + fib(100) + fib(100)
  println(s"Sum of fib(100) * 3 = ${sum} and time taken is ${(System.currentTimeMillis() - t1) / 1000.0} seconds")

  val t2 = System.currentTimeMillis()
  val future1 = Future(fib(100))
  val future2 = Future(fib(100))
  val future3 = Future(fib(100))
  val finalFuture: Future[Int] = for {
    a <- future1
    b <- future2
    c <- future3
  } yield a + b + c

  finalFuture onComplete {
    case Success(value) => {
      val endTime = ((System.currentTimeMillis() - t2) / 1000.0)
      println(s"Sum of fib(100) * 3 executed in parallel is ${value} and total time taken ${endTime} seconds")
    }
    case Failure(x) => println("Failed: $x")
  }

  Thread.sleep(5000)

}

object Fib {

  def fib(n: Int): Int = {
    @tailrec
    def go(n: Int, a: Int, b: Int): Int = n match {
      case 0 => a
      case _ => go(n - 1, b, a + b)
    }

    go(n, 0, 1)
  }
}
