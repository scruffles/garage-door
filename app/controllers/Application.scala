package controllers

import java.util.{TimerTask, Timer}

import com.pi4j.io.gpio.event.{GpioPinDigitalStateChangeEvent, GpioPinListenerDigital}
import com.pi4j.io.gpio.{RaspiPin, PinPullResistance, GpioFactory}
import play.api.libs.iteratee.{Enumerator, Concurrent, Iteratee}
import play.api.mvc._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

//  val listeners = new mutable.MutableList[Listener]()
  var value = "closed"

  val gpio = GpioFactory.getInstance()
  val pin14 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_14, PinPullResistance.PULL_DOWN)

//  def fireListeners() = {
//    listeners.foreach {
//      listener => listener.valueChanged()
//    }
//  }

  def setStatus(status: String) = Action {
//    setStatusImpl(status)
    Ok("ok")
  }

  def triggerDoor() = {
//    val newStatus = if (value == "open") "closed" else "open"
//    setStatusImpl(if (value == "open")  "closing" else "opening")
//    val task = new TimerTask {
//      override def run(): Unit = setStatusImpl(newStatus)
//    }
//    new Timer().schedule(task, 3000)
  }

//  def setStatusImpl(status: String) = {
//    value = status
//    fireListeners()
//  }

  def statusSocket() = WebSocket.using[String] { request =>

    val (out, channel) = Concurrent.broadcast[String]

    val in = Iteratee.foreach[String]{in =>
      if (in == "request-status") {
        channel.push(if (pin14.isHigh) "open" else "closed")
//        fireListeners()
      } else if (in == "trigger-door") {
        triggerDoor()
      } else {
        channel.push("Invalid request")
      }
    }

//    listeners += new Listener() {
//      override def valueChanged(): Unit = channel.push(value)
//    }

    pin14.addListener(new GpioPinListenerDigital {
      override def handleGpioPinDigitalStateChangeEvent(gpioPinDigitalStateChangeEvent: GpioPinDigitalStateChangeEvent): Unit = {
        channel.push(if (pin14.isHigh) "open" else "closed")
      }
    })

    (in, out)
  }

  trait Listener {
    def valueChanged()
  }

}