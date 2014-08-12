package controllers

/**
 *
 */
trait Hardware {
  def addListener(listener: Listener)
  def currentDoorState(): String // todo constant?
  def triggerDoor()
}

trait Listener {
  def valueChanged()
}

