package controllers

import java.util.{TimerTask, Timer}

import scala.collection.mutable


/**
 *
 */
class MockHardware extends Hardware {
  val listeners = new mutable.MutableList[Listener]()
  var value = "closed"

  def triggerDoor() = {
    val newStatus = if (value == "open") "closed" else "open"
    setStatusImpl(if (value == "open") "closing" else "opening")
    val task = new TimerTask {
      override def run(): Unit = setStatusImpl(newStatus)
    }
    new Timer().schedule(task, 3000)
  }

  def setStatusImpl(status: String) = {
    value = status
    fireListeners()
  }


  def currentDoorState() = {
    value
  }

  def addListener(listener: Listener) = {
    listeners += listener
  }

  def fireListeners() = listeners.foreach(_.valueChanged())
}