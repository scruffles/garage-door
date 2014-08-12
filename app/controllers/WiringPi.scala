package controllers

import com.pi4j.io.gpio.RaspiPin

/**
 * pi4j follows a pin numbering scheme which is different than documented with Raspberry Pi and all of its books.
 * The scheme mimics the wikingpi library (http://wiringpi.com/pins/).  While the reasoning makes some sense, I'm using
 * mappings here so I don't have to map this in my head every time I look at it.
 */
object WiringPi {
  val Pin21rev1 = RaspiPin.GPIO_02
  val Pin27rev2 = RaspiPin.GPIO_02
  val Pin22 = RaspiPin.GPIO_03
  val Pin23 = RaspiPin.GPIO_04
  val Pin24 = RaspiPin.GPIO_05
}
