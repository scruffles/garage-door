package controllers

import com.pi4j.io.gpio.event.{GpioPinDigitalStateChangeEvent, GpioPinListenerDigital}
import controllers.RaspberryPiHardware._
import play.api.libs.iteratee.{Concurrent, Iteratee}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

/**
 *
 */
object DiagnosticsController extends Controller {

  def diagnosticsSocket() = WebSocket.using[String] { request =>
    val (out, channel) = Concurrent.broadcast[String]

    isOpenSensor.addListener(new GpioPinListenerDigital {
      override def handleGpioPinDigitalStateChangeEvent(event: GpioPinDigitalStateChangeEvent): Unit =
        channel.push("open changed to " + event.getState)
    })
    isClosedPin.addListener(new GpioPinListenerDigital {
      override def handleGpioPinDigitalStateChangeEvent(event: GpioPinDigitalStateChangeEvent): Unit =
        channel.push("closed changed to " + event.getState)
    })

    val in = Iteratee.foreach[String](println)

    (in, out)
  }


}
