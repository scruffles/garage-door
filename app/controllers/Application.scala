package controllers

import play.api.libs.iteratee.{Concurrent, Iteratee}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  val hardware = new MockHardware()

  def statusSocket() = WebSocket.using[String] { request =>

    val (out, channel) = Concurrent.broadcast[String]

    val in = Iteratee.foreach[String]{in =>
      if (in == "request-status") {
        channel.push(hardware.currentDoorState())
      } else if (in == "trigger-door") {
        hardware.triggerDoor()
      } else {
        channel.push("Invalid request")
      }
    }

    hardware.addListener(new Listener() {
      override def valueChanged(): Unit = {
        channel.push(hardware.currentDoorState())
      }
    })

    (in, out)
  }
}