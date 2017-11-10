package com.kamoche.chapter6


case class StockValue(value: Double, time: Long = System.currentTimeMillis())

//command
case class ValueUpdate(newValue: Double)

//event
sealed trait StockEvent

case class ValueAppended(stockValue: StockValue) extends StockEvent

//state
case class StockHistory(values: Vector[StockValue] = Vector.empty[StockValue]) {

  def update(stockEvent: StockEvent) = stockEvent match {
    case ValueAppended(stockValue) => copy(values :+ stockValue)
  }

  override def toString: String = s"$values"
}