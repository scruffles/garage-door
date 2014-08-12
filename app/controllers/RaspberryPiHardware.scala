package controllers

import com.pi4j.io.gpio.event.{GpioPinDigitalStateChangeEvent, GpioPinListenerDigital}
import com.pi4j.io.gpio.{PinState, GpioFactory, PinPullResistance, RaspiPin}

import scala.collection.mutable

/**
 *
 */
class RaspberryPiHardware extends Hardware {
  // todo remove listeners at some point
  // todo constant for open/closed ?
  // todo shutdown cleanly - http://pi4j.com/example/shutdown.html
  val gpio = GpioFactory.getInstance()
  val isOpenSensor = gpio.provisionDigitalInputPin(WiringPi.Pin22, PinPullResistance.PULL_DOWN)
  val isClosedPin = gpio.provisionDigitalInputPin(WiringPi.Pin23, PinPullResistance.PULL_DOWN)
  val triggerDoorPin = gpio.provisionDigitalOutputPin(WiringPi.Pin24, PinState.LOW)

  var lastValue = currentDoorState()
  val listeners = new mutable.MutableList[Listener]()

  override def addListener(listener: Listener) = listeners += listener

  isOpenSensor.addListener(new GpioPinListenerDigital {
    override def handleGpioPinDigitalStateChangeEvent(evt: GpioPinDigitalStateChangeEvent) = {
      lastValue = "open"
      fireListeners()
    }
  })
  isClosedPin.addListener(new GpioPinListenerDigital {
    override def handleGpioPinDigitalStateChangeEvent(evt: GpioPinDigitalStateChangeEvent) = {
      lastValue = "closed"
      fireListeners()
    }
  })

  override def triggerDoor(): Unit = triggerDoorPin.pulse(1000, PinState.HIGH)

  override def currentDoorState(): String =
    if (isOpenSensor.isHigh) "open"
    else if (isOpenSensor.isHigh) "closed"
    else if (lastValue == "open") "closing"
    else if (lastValue == "closed") "opening"
    else "?"

  def fireListeners() = listeners.foreach(_.valueChanged())
}
