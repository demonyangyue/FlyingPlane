package com.example

/**
 * @author yy
 */
import akka.actor.{Props, Actor, ActorSystem, ActorLogging}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Altimeter{
    case object Tick
    case class RateChange(amount: Float)
}

class Altimeter extends Actor with ActorLogging with EventSource{
    import Altimeter._

    val maxRateOfClimb = 5000
    var rateOfClimb: Float = 0
    var altitude: Double = 0
    var lastTick = System.currentTimeMillis

    val ticker = context.system.scheduler.schedule(100.millis, 100.millis, self, Tick)
    def altimeterReceive: Receive = {
        case RateChange(amount) => {
            rateOfClimb = amount.min(1.0f).max(-1.0f)*maxRateOfClimb
            log.info(s"Altimeter changed rate of climb to $rateOfClimb")
        }
        case Tick => {
            val tick = System.currentTimeMillis
            altitude = altitude + (tick - lastTick) * rateOfClimb / 60000.0
            notify(ControlSurfaces.AltitudeUpdate(altitude))
            lastTick = tick
        }
    } 

    def receive = altimeterReceive orElse eventSourceReceive
    override def postStop(): Unit = ticker.cancel
}
